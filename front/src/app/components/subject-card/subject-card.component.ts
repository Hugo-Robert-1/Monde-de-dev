import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { RouterModule } from '@angular/router';
import { SubjectIsSubscribed } from 'src/app/features/subjects/interfaces/subject.interface';

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
  @Input() subject!: SubjectIsSubscribed;

  @Output() subscribe = new EventEmitter<SubjectIsSubscribed>();

  constructor() { }

  onSubscribe(): void {
    this.subscribe.emit(this.subject)
  }
}
