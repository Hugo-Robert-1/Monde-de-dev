import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Subject } from '../interfaces/subject.interface';
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
}
