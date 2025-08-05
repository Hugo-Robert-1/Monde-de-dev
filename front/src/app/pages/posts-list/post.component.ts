import { Component, OnInit } from '@angular/core';
import { PostApiService } from '../../features/posts/services/post-api.service';
import { Post } from '../../features/posts/interfaces/post.interface';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss']
})
export class PostComponent implements OnInit {

  posts: Post[] = [];
  error: string | null = null;
  sortAsc = false;

  private subscription?: Subscription;

  constructor(private postApiService: PostApiService) {}

  ngOnInit(): void {
    this.fetchPosts();
  }

  toggleSort(): void {
    this.sortAsc = !this.sortAsc;
    this.fetchPosts();
  }

  fetchPosts(): void {
    const order = this.sortAsc ? 'asc' : 'desc';
    this.subscription?.unsubscribe();
    this.subscription = this.postApiService.getPostsFromSubscribedSubjects(order).subscribe({
      next: (data) => {
        this.posts = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des articles', err);
        this.error = 'Error while loading the posts';
      }
    });
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }
}
