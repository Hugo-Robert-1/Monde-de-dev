import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';
import { SessionService } from 'src/app/features/sessions/services/session.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {
  isAuthPageMobile = false;
  isAuthPage = false;

  private routerSub?: Subscription;
  
  constructor(private router: Router, private sessionService: SessionService) {}

  ngOnInit(): void {
    this.updateVisibility();

    this.routerSub = this.router.events
    .pipe(filter(event => event instanceof NavigationEnd))
    .subscribe(() => {
      this.updateVisibility();
    });
  }

  disconnect(): void {
    this.sessionService.logOut();
  }

  ngOnDestroy(): void {
    this.routerSub?.unsubscribe();
  }

  @HostListener('window:resize')
  onResize() {
    this.updateVisibility();
  }

  updateVisibility(): void {
    const url = this.router.url;
    const isAuthRoute = ['/login', '/register'].includes(url);

    const isSmallScreen = window.matchMedia('(max-width: 599px)').matches;
    this.isAuthPage = isAuthRoute;
    this.isAuthPageMobile = isAuthRoute && isSmallScreen;
  }

  get isHomePage(): boolean {
    return this.router.url === '/';
  }
}