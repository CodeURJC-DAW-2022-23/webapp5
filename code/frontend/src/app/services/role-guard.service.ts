import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UserProfile } from '../models/user.rest.model';
import { catchError, map} from 'rxjs/operators';
import { of } from 'rxjs';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(private authService: UserService, private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> | Promise<boolean> | boolean {
    return this.authService.getMe().pipe(
      map((response: UserProfile) => {
        console.log('Response:', response);
        if (!response) {
          this.router.navigate(['/login']);
          return false;
        }
        if (response.user.roles.indexOf('ADMIN') === -1){
          this.router.navigate(['error/403']);
          return false;
        }
        return true;
      }),
      catchError((error: any) => {
        console.error('Error:', error);
        this.router.navigate(['/login']);
        return of(false);
      })
    ) as Observable<boolean>;
  }
}
