import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { RouterModule } from '@angular/router';
import { Subject, SubjectIsSubscribed } from 'src/app/features/subjects/interfaces/subject.interface';

type SubjectCardInput = Subject | SubjectIsSubscribed;

@Component({
  selector: 'app-subject-card',
  templateUrl: './subject-card.component.html',
  styleUrls: ['./subject-card.component.scss'],
  standalone: true,
  imports: [
    MatCardModule,
    RouterModule,
    MatButtonModule
  ]
})
export class SubjectCardComponent {
  @Input() mode: 'subscribe' | 'unsubscribe' = 'subscribe';

  @Input() subject!: SubjectCardInput;

  @Output() action = new EventEmitter<SubjectCardInput>();

  constructor() { }

  get isSubscribed(): boolean {
    return this.mode === 'unsubscribe' || 
           (this.mode === 'subscribe' && (this.subject as SubjectIsSubscribed).isSubscribed);
  }

  get buttonLabel(): string {
    return this.mode === 'unsubscribe' ? 'Se désabonner' : (this.isSubscribed ? 'Déjà abonné' : "S'abonner");
  }

  get buttonDisabled(): boolean {
    return this.mode === 'subscribe' && this.isSubscribed;
  }

  onButtonClick(): void {
    if (this.mode === 'unsubscribe' || !this.isSubscribed) {
      this.action.emit(this.subject);
    }
  }
}
