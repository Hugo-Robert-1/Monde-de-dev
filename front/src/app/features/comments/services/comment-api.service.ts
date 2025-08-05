import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Comment, CommentCreate } from '../interfaces/comment.interface';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CommentApiService {

  private pathService = `${environment.apiUrl}/api/comment`;

  constructor(private httpClient: HttpClient) {
  }

  public create(commentCreate: CommentCreate): Observable<Comment> {
    return this.httpClient.post<Comment>(this.pathService, commentCreate);
  }
}