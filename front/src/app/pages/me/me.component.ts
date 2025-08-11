import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { tap, switchMap, catchError, of, Subject, takeUntil, BehaviorSubject, finalize } from 'rxjs';
import { optionalStrongPasswordValidator } from 'src/app/features/auth/validators/optionalStrongPasswordValidator';
import { SessionInformation } from 'src/app/features/sessions/interfaces/sessionInformation.interface';
import { SessionService } from 'src/app/features/sessions/services/session.service';
import { Subject as Topic } from 'src/app/features/subjects/interfaces/subject.interface';
import { SubjectApiService } from 'src/app/features/subjects/services/subject-api.service';
import { User, UserUpdated, UserWithSubjects } from 'src/app/features/user/interfaces/user.interface';
import { UserApiService } from 'src/app/features/user/services/user-api.service';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss']
})
export class MeComponent implements OnInit, OnDestroy {
  subjects!: UserWithSubjects;
  subscribedSubjects: Topic[] = [];
  public onError = false;
  me: User = {
    id: 0,
    username: '',
    email: '',
    createdAt: new Date(),
    updatedAt: new Date()
  };
  
  public form = this.fb.group({
    email: [
      '',
      [
        Validators.required,
        Validators.email
      ]
    ],
    username: [
      '',
      [
        Validators.required,
        Validators.minLength(3)
      ]
    ],
    password: [
      '',
      [
        optionalStrongPasswordValidator(),
      ]
    ]
  });

  public updateResult$ = new Subject<boolean>();
  public isSubmitting = false;

  private destroy$ = new Subject<void>();

  private subjects$ = new BehaviorSubject<Topic[]>([]);
  readonly subjectsObservable$ = this.subjects$.asObservable();
  
  constructor(private fb: FormBuilder,
              private userApiService: UserApiService,
              private sessionService: SessionService,
              private subjectApiService: SubjectApiService) {
  }

  ngOnInit(): void {
    this.userApiService.me().pipe(
      takeUntil(this.destroy$),
      tap((user) => {
        this.me = user;
        this.form.patchValue({
          email: user.email ?? '',
          username: user.username ?? ''
          // password laissé vide
        });
      }),
      switchMap(user => this.userApiService.getSubjectsForCurrentUser(user.id)),
      tap((userWithSubjects) => {
        this.subjects$.next(userWithSubjects.subscribedSubjects);
      }),
      catchError((err) => {
        console.error('Erreur lors du chargement du profil ou des sujets', err);
        return of(null);
      })
    ).subscribe();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
  
  onSubmit(): void {
    if (this.form.invalid || !this.me?.id) {
      return;
    }

    this.isSubmitting = true;

    const formValue = this.form.value;

    const userUpdated: UserUpdated = {
      username: formValue.username?.trim() || '',
      email: formValue.email?.trim() || '',
      password: formValue.password ? formValue.password.trim() : ''
    };

    this.userApiService.update(this.me.id, userUpdated).pipe(
      tap((res: SessionInformation) => {
        this.sessionService.setAccessToken(res.accessToken);
        this.updateResult$.next(true);
      }),
      catchError((error: any) => {
        console.error('Erreur lors de la mise à jour du profil :', error);
        this.updateResult$.next(false);
        return of(null);
      }),
      finalize(() => {
        this.isSubmitting = false;
      })
    ).subscribe();
  }


  public onUnsubscribe(subject: Topic): void {
    this.subjectApiService.unsubscribe(subject.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
        const updatedSubjects = this.subjects$.value.filter(s => s.id !== subject.id);
        this.subjects$.next(updatedSubjects);
      },
      error: (err) => {
        console.error(`Erreur lors du désabonnement au sujet ${subject.id}`, err);
      }
    });
  }
}
