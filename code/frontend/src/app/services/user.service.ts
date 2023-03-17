import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserProfile } from '../models/user.rest.model';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

const BASE_URL = '/api/users/';

@Injectable({ providedIn: 'root' })
export class UserService {

	constructor(private http: HttpClient) { }

  getMe(): Observable<UserProfile> {
    return this.http.get(BASE_URL + '/me', { withCredentials: true }
    ) as Observable<UserProfile>;}

  getRecomendations(n: number): Observable<any> {
    return this.http.get(BASE_URL + '/recomended?numberOfGames=' + n)
  }

  getProfileImage(user: User) {
    return user.profilePircture ? BASE_URL + user.id + '/imageProfile' : '/assets/images/no_image.png';
  }

};
