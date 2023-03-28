import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserProfile } from '../models/user.rest.model';
import { Observable, throwError } from 'rxjs';
import { User } from '../models/user.model';
import { catchError } from 'rxjs/operators';

const BASE_URL = '/api/users/';

@Injectable({ providedIn: 'root' })
export class UserService {
  constructor(private http: HttpClient) {}

  getMe(): Observable<UserProfile> {
    return this.http.get(BASE_URL + 'me', {
      withCredentials: true,
    }) as Observable<UserProfile>;
  }

  getRecomendations(n: number): Observable<any> {
    return this.http.get(BASE_URL + 'recomended?numberOfGames=' + n);
  }

  getProfileImage(user: User) {
    return user.profilePircture
      ? BASE_URL + user.id + '/imageProfile'
      : '/assets/images/no_image.png';
  }

  editUser(id: number, formData): Observable<any> {
    return this.http.put(BASE_URL + id, formData).pipe(
      catchError((error) => {
        return throwError(error);
      })
    );
  }

  addToCart(gameId: number, userId: number): Observable<any> {
    return this.http.post(BASE_URL + userId + '/cart/' + gameId, null).pipe(
      catchError((error) => {
        return throwError(error);
      })
    );
  }

  viewCart(userId: number): Observable<any> {
    return this.http.get(BASE_URL + userId + '/cart').pipe(
      catchError((error) => {
        return throwError(error);
      })
    );
  }

  deleteFromCart(gameId: number, userId: number): Observable<any> {
    return this.http.delete(BASE_URL + userId + '/cart/' + gameId).pipe(
      catchError((error) => {
        return throwError(error);
      })
    );
  }

  getMoreCartGames(userId: number, index: number) : Observable<any> {
    return this.http.get(BASE_URL + userId + '/moreCartGames/' + index).pipe(
      catchError((error) => {
        return throwError(error);
      })
    );
  }

  checkOut(userId: number, formData): Observable<any> {
    return this.http.post(BASE_URL + userId + '/checkout', formData).pipe(
      catchError((error) => {
        return throwError(error);
      })
    );
  }

  }

