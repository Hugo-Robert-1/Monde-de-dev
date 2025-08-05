import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PostApiService } from 'src/app/features/posts/services/post-api.service';
import { Subject } from 'src/app/features/subjects/interfaces/subject.interface';
import { SubjectApiService } from 'src/app/features/subjects/services/subject-api.service';
import { Subject as RxSubject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-post-create',
  templateUrl: './post-create.component.html',
  styleUrls: ['./post-create.component.scss']
})
export class PostCreateComponent implements OnInit {
  onError = false;
  subjects : Subject[] = [];
  articleForm!: FormGroup;
  private destroy$ = new RxSubject<void>();

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private subjectApiService: SubjectApiService,
    private postApiService: PostApiService
  ) {}

  ngOnInit() {
    this.articleForm = this.fb.group({
      title: ['', Validators.required],
      content: ['', Validators.required],
      subjectId: [null, Validators.required],
    });

    this.loadSubjects();
  }

  loadSubjects(): void {
    this.subjectApiService.getAll()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (subjects) => this.subjects = subjects,
        error: (err) => console.error('Erreur chargement sujets :', err)
      });
  }
  
  submit(): void {
    if (this.articleForm.valid) {
      const formValue = this.articleForm.value;
      console.log(formValue);
      this.postApiService.create(formValue)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: () => this.router.navigate(['/posts']),
          error: () => this.onError = true
        });
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
