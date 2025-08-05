import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject, Subscription } from 'rxjs';
import { CommentApiService } from 'src/app/features/comments/services/comment-api.service';
import { PostDetailWithComments } from 'src/app/features/posts/interfaces/post.interface';
import { PostApiService } from 'src/app/features/posts/services/post-api.service';
import { Comment, CommentCreate } from 'src/app/features/comments/interfaces/comment.interface';

@Component({
  selector: 'app-post-details',
  templateUrl: './post-details.component.html',
  styleUrls: ['./post-details.component.scss']
})
export class PostDetailsComponent implements OnInit, OnDestroy  {
  private postDetailWithComments = new BehaviorSubject<PostDetailWithComments| undefined>(undefined) ;
  public postDetails$ = this.postDetailWithComments.asObservable();
  error: string | null = null;
  inputComment: String = '';

  onError: boolean = false;
  public idPost : Number;

  private subscription?: Subscription;

  constructor(
    private postApiService: PostApiService,
    private commentApiService: CommentApiService,
    private route: ActivatedRoute,
  ) {
    this.idPost = this.route.snapshot.params['id'];
  }

  ngOnInit(): void {
    this.loadPost();
  }

  private loadPost(): void {
    this.subscription?.unsubscribe();
    this.subscription = this.postApiService.getOneById(this.idPost).subscribe({
      next: (data) => this.postDetailWithComments.next(data),
      error: (err) => {
        console.error('Erreur lors du chargement des articles', err);
        this.error = 'Error while loading the posts';
      }
    });
    console.log('loadpost');
  }
  
  public submitMessage(): void {
    const trimmedContent = this.inputComment.trim();

    if (!trimmedContent) {
      this.onError = true;
      return;
    }

    const commentToSend: CommentCreate = {
      content: trimmedContent,
      postId: +this.idPost
    };

    this.commentApiService.create(commentToSend).subscribe({
      next: (response: Comment) => {
        if (this.postDetailWithComments.value) {
          const updatedPost: PostDetailWithComments = {
            ...this.postDetailWithComments.value,
            commentaires: [...this.postDetailWithComments.value.commentaires, response]
          };
          this.postDetailWithComments.next(updatedPost);
      }
        this.inputComment = '';
        this.onError = false;
      },
      error: error => {
        this.onError = true;
      },
    });
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

}
