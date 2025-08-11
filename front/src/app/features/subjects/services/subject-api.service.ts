import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Subject, SubjectIsSubscribed } from '../interfaces/subject.interface';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SubjectApiService {

  private pathService = `${environment.apiUrl}/api/subject`;

  constructor(private httpClient: HttpClient) {
  }

  public getAll(): Observable<Subject[]> {
    return this.httpClient.get<Subject[]>(this.pathService);
  }

  public getSubjectsWithSubscriptionStatus(): Observable<SubjectIsSubscribed[]> {
    return this.httpClient.get<SubjectIsSubscribed[]>(this.pathService + '/with-subscription-status')
  }

  public subscribe(subjectId: number): Observable<void> {
    return this.httpClient.post<void>(`${this.pathService}/${subjectId}/subscribe`, {});
  }

  public unsubscribe(subjectId: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.pathService}/${subjectId}/unsubscribe`, {});
  }
}
