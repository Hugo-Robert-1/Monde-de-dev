import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { SubjectIsSubscribed } from 'src/app/features/subjects/interfaces/subject.interface';
import { SubjectApiService } from 'src/app/features/subjects/services/subject-api.service';

@Component({
  selector: 'app-subjects-list',
  templateUrl: './subjects-list.component.html',
  styleUrls: ['./subjects-list.component.scss']
})
export class SubjectsListComponent implements OnInit, OnDestroy {
  subjects: SubjectIsSubscribed[] = [];
  private destroy$ = new Subject<void>();

  constructor(private subjectApiService: SubjectApiService) { }

  ngOnInit(): void {
    this.loadSubjects();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadSubjects(): void {
    this.subjectApiService.getSubjectsWithSubscriptionStatus()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (subjects) => {
          this.subjects = subjects;
        },
        error: (err) => {
          console.error('Erreur lors du chargement des sujets', err);
        }
      });
  }

  onSubscribe(subject: SubjectIsSubscribed): void {
    this.subjectApiService.subscribe(subject.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          subject.isSubscribed = true;
        },
        error: (err) => {
          console.error(`Erreur lors de l’abonnement au sujet ${subject.id}`, err);
        }
      });
  }
}
