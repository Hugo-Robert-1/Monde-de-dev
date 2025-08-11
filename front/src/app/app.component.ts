import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from './features/sessions/services/session.service';
import { Observable, take } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  constructor(
    private router: Router,
    private sessionService: SessionService) {
  }

  public $isLogged(): Observable<boolean> {
    return this.sessionService.isLoggedIn$;
  }

  ngOnInit() {
    this.sessionService.autoLogin().pipe(take(1)).subscribe((isLoggedIn) => {
    if (isLoggedIn && this.router.url === '/') {
      this.router.navigate(['/posts']);
    }
  });
  }

  public logout(): void {
    this.sessionService.logOut();
    this.router.navigate([''])
  }

  get isHomePage(): boolean {
    return this.router.url === '/';
  }
}
