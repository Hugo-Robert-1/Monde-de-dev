import {Injectable} from "@angular/core";
import {CanActivate, Router} from "@angular/router"; 
import { SessionService } from "../features/sessions/services/session.service";
import { map, Observable, take } from "rxjs";

@Injectable({providedIn: 'root'})
export class UnauthGuard implements CanActivate {

  constructor( 
    private router: Router,
    private sessionService: SessionService,
  ) {
  }

   canActivate(): Observable<boolean> {
    return this.sessionService.isLoggedIn$.pipe(
      take(1), // On prend la dernière valeur puis on complète l'Observable
      map(isLoggedIn => {
        if (isLoggedIn) {
          this.router.navigate(['posts']); // Redirige si déjà connecté
          return false; // Bloque la route
        }
        return true; // Permet d'accéder à la route
      })
    );
  }
}