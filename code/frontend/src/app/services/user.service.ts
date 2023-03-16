import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserProfile } from '../models/user.rest.model';
import { Observable } from 'rxjs';

const BASE_URL = '/api/users/';

@Injectable({ providedIn: 'root' })
export class UserService {

	constructor(private http: HttpClient) { }

  getMe(): Observable<UserProfile> {
    return this.http.get(BASE_URL + '/me', { withCredentials: true }
    ) as Observable<UserProfile>;}

};
