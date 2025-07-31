import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './features/auth/services/auth.service';
import { SessionService } from './features/sessions/services/session.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  constructor(
    private authService: AuthService,
    private router: Router,
    private sessionService: SessionService) {
  }

  public $isLogged(): Observable<boolean> {
    return this.sessionService.isLoggedIn$;
  }

  title = 'front';

  public logout(): void {
    this.sessionService.logOut();
    this.router.navigate([''])
  }

  get isHomePage(): boolean {
    return this.router.url === '/';
  }
}
