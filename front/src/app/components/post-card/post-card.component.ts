import { DatePipe } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { MatCard, MatCardModule } from '@angular/material/card';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-post-card',
  templateUrl: './post-card.component.html',
  styleUrls: ['./post-card.component.scss'],
  standalone: true,
  imports: [
    MatCardModule,
    DatePipe,
    RouterModule
  ]
})
export class PostCardComponent {
  @Input() date: Date | undefined;
  @Input() auteur: String = '';
  @Input() content: String = '';
  @Input() title: String = '';
  @Input() postId: Number = 0;

  constructor() { }
}
