import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, catchError, map, Observable, of, tap } from "rxjs";
import { SessionInformation } from "../interfaces/sessionInformation.interface";
import { environment } from "src/environments/environment";

@Injectable({ providedIn: 'root' })
export class SessionService {
  private accessToken: string | null = null;
  private sessionInformation: SessionInformation | null = null;
  private _isLoggedIn$ = new BehaviorSubject<boolean>(this.hasValidAccessToken());
  public isLoggedIn$ = this._isLoggedIn$.asObservable();

  constructor(private http: HttpClient) {}

  getAccessToken(): string | null {
    return this.accessToken;
  }

  getSessionInformation(): SessionInformation | null {
    return this.sessionInformation;
  }

  public logIn(user: SessionInformation): void {
    this.sessionInformation = user;
    this.accessToken = user.accessToken;
    this._isLoggedIn$.next(true);
  }

  logOut() {
    this.accessToken = null;
    this.sessionInformation = null;
    this._isLoggedIn$.next(false);
  }

  setAccessToken(token: string): void {
    this.accessToken = token;
    this._isLoggedIn$.next(true);
  }

  private hasValidAccessToken(): boolean {
    return !!this.accessToken;
  }
}