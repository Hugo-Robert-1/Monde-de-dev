import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Post, PostCreate } from '../interfaces/post.interface';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PostApiService {

  private pathService = `${environment.apiUrl}/api/post`;

  constructor(private httpClient: HttpClient) {
  }

  public getPostsFromSubscribedSubjects(order : String): Observable<Post[]> {
    return this.httpClient.get<Post[]>(this.pathService + `/subscribed?order=${order}`);
  }

  public create(postCreate: PostCreate) {
    return this.httpClient.post<PostCreate>(this.pathService, postCreate);
  }
}