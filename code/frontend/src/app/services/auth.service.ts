import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserProfile } from '../models/user.rest.model';
import { catchError, map } from 'rxjs/operators';
import { Observable, throwError } from 'rxjs';

const BASE_URL = '/api/auth/';

@Injectable({ providedIn: 'root' })
export class LoginService {

    logged: boolean;
    userProfile : UserProfile;

    constructor(private http: HttpClient) {
        this.reqIsLogged();
    }

    reqIsLogged() {

        this.http.get('/api/users/me', { withCredentials: true }).subscribe(
            response => {
                this.userProfile = response as UserProfile;
                this.logged = true;
            },
            error => {
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
            console.error('Error during login:', error);
            return throwError('Something went wrong');
          })
        );
    }

    logOut() {

        return this.http.post(BASE_URL + '/logout', { withCredentials: true })
            .subscribe((resp: any) => {
                console.log("LOGOUT: Successfully");
                this.logged = false;
                this.userProfile = undefined;
            });

    }

    register(formName: string, formLastName: string, formMail: string, formPassword: string, formAboutMe: string): Observable<any> {
      return this.http.post("/api/users/", {name: formName, lastName: formLastName, mail: formMail, encodedPassword: formPassword, aboutMe: formAboutMe})
        .pipe(
          map((response: any) => {
            console.log(response);
            return response;
          }),
          catchError((error: any) => {
            console.error('Error during login:', error);
            return throwError('Something went wrong');
          })
        );
    }

    isLogged() {
        return this.logged;
    }

    isAdmin() {
        return this.userProfile.user && this.userProfile.user.roles.indexOf('ADMIN') !== -1;
    }

    currentUser() {
        console.log("hola" + this.userProfile.user.name)
        return this.userProfile.user;
    }

    currentUserProfile(){
      return this.userProfile;
    }
}
