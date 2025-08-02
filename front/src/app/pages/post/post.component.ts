import { Component, OnInit } from '@angular/core';
import { PostApiService } from '../../features/posts/services/post-api.service';
import { Post } from '../../features/posts/interfaces/post.interface';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss']
})
export class PostComponent implements OnInit {

  posts: Post[] = [];
  error: string | null = null;

  constructor(private postApiService: PostApiService) {}

  ngOnInit(): void {
    this.postApiService.getPostsFromSubscribedSubjects().subscribe({
      next: (data) => {
        this.posts = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des posts', err);
        this.error = 'Impossible de charger les articles';
      }
    });
  }

}
