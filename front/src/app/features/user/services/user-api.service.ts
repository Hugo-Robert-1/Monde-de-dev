import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User, UserUpdated, UserWithSubjects } from '../interfaces/user.interface';
import { environment } from 'src/environments/environment';
import { SessionInformation } from '../../sessions/interfaces/sessionInformation.interface';

@Injectable({
  providedIn: 'root'
})
export class UserApiService {

  private pathService = `${environment.apiUrl}/api/user`;

  constructor(private httpClient: HttpClient) {
  }

  public getSubjectsForCurrentUser(postId : number): Observable<UserWithSubjects> {
    return this.httpClient.get<UserWithSubjects>(this.pathService + `/${postId}/subjects`);
  }

  public update(postId: number, userUpdated: UserUpdated): Observable<SessionInformation> {
    return this.httpClient.put<SessionInformation>(this.pathService + `/${postId}`, userUpdated);
  }

  public me(): Observable<User> {
    return this.httpClient.get<User>(`${environment.apiUrl}/api/auth/me`);
  }
}