import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../models/user.model';
import { UserProfile } from '../models/user.rest.model';

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
                if (error.status != 404) {
                    console.error('Error when asking if logged: ' + JSON.stringify(error));
                }
            }
        );

    }

    logIn(user: string, pass: string) {

        this.http.post(BASE_URL + "/login", { username: user, password: pass }, { withCredentials: true })
            .subscribe(
                (response) => this.reqIsLogged(),
                (error) => alert("Wrong credentials")
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

    isLogged() {
        return this.logged;
    }

    isAdmin() {
        return this.userProfile.user && this.userProfile.user.roles.indexOf('ADMIN') !== -1;
    }

    currentUser() {
        return this.userProfile.user;
    }

    currentUserProfile(){
      return this.userProfile;
    }
}
