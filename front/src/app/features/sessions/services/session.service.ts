import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, catchError, map, Observable, of, tap } from "rxjs";
import { SessionInformation } from "../interfaces/sessionInformation.interface";
import { environment } from "src/environments/environment";
import { Router } from "@angular/router";
import { User } from "../../user/interfaces/user.interface";

@Injectable({ providedIn: 'root' })
export class SessionService {
  private accessToken: string | null = null;
  private sessionInformation: SessionInformation | null = null;
  private _isLoggedIn$ = new BehaviorSubject<boolean>(this.hasValidAccessToken());
  public isLoggedIn$ = this._isLoggedIn$.asObservable();
  private meSubject = new BehaviorSubject<User | null>(null);
  public me$ = this.meSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {}

  getAccessToken(): string | null {
    return this.accessToken;
  }

  getSessionInformation(): SessionInformation | null {
    return this.sessionInformation;
  }

  public logIn(user: SessionInformation): void {
    this.accessToken = user.accessToken;
    this._isLoggedIn$.next(true);
  }

  logOut() {
    this.accessToken = null;
    this._isLoggedIn$.next(false);
    this.router.navigate([''])
  }

  // Login automatique au démarrage
  autoLogin(): Observable<boolean> {
    return this.refreshToken().pipe(
      tap(response => {
        this.accessToken = response.accessToken;
        this._isLoggedIn$.next(true);
      }),
      map(() => true),
      catchError(() => {
        this.logOut();
        return of(false);
      })
    );
  }

  // Rafraîchissement via cookie httpOnly
  refreshToken(): Observable<{ accessToken: string }> {
    return this.http.post<{ accessToken: string }>(
      `${environment.apiUrl}/api/auth/refresh-token`,
      {},
      { withCredentials: true } // Envoie le cookie HttpOnly
    );
  }

  setAccessToken(token: string): void {
    this.accessToken = token;
    this._isLoggedIn$.next(true);
  }

  private hasValidAccessToken(): boolean {
    return !!this.accessToken;
  }

  setMe(user: User): void {
    this.meSubject.next(user);
  }
}