import { UserService } from './user.service';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserProfile } from '../models/user.rest.model';
import { catchError, map } from 'rxjs/operators';
import { Observable, throwError } from 'rxjs';

const BASE_URL = '/api/auth';

@Injectable({ providedIn: 'root' })
export class LoginService {

    logged: boolean;
    userProfile : UserProfile;
    currentCart: any;

    constructor(private http: HttpClient, private userService: UserService) {
        this.reqIsLogged();
    }

    reqIsLogged() {

        this.http.get('/api/users/me', { withCredentials: true }).subscribe(
            response => {
                this.userProfile = response as UserProfile;
                this.logged = true;
                this.userService.viewCart(this.userProfile.user.id).subscribe(
                  response => {
                    this.currentCart = response;
                  }
                );
            },
            _ => {
                  throw new Error('Something bad happened');
            }
        );

    }

    logIn(user: string, pass: string): Observable<any> {
      return this.http.post(BASE_URL + "/login", { username: user, password: pass }, { withCredentials: true })
        .pipe(
          map((response: any) => {
            this.reqIsLogged();
            return response;
          }),
          catchError((error: any) => {
            return throwError('Something went wrong');
          })
        );
    }

    logOut() {

        return this.http.post(BASE_URL + '/logout', { withCredentials: true })
            .subscribe((resp: any) => {
                this.logged = false;
                this.userProfile = undefined;
            });

    }

    register(formData): Observable<any> {
      return this.http.post("/api/users/", formData)
        .pipe(
          map((response: any) => {
            return response;
          }),
          catchError((error: any) => {
            return throwError('Something went wrong');
          })
        );
    }

    isLogged() {
        return this.logged;
    }

    isAdmin() {
        return this.isLogged() && this.userProfile.user.roles.indexOf('ADMIN') !== -1;
    }

    currentUser() {
        return this.userProfile.user;
    }

    currentUserProfile(){
      return this.userProfile;
    }

    getCurrentCart(){
      return this.currentCart;
    }
}
