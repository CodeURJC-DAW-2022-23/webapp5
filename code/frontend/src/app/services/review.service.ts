import { Game } from './../models/game.model';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

const BASE_URL = '/api/reviews/';

@Injectable({ providedIn: 'root' })
export class ReviewService {


	constructor(private http: HttpClient) { }

    getMoreReviews(id: number, indexReview: number): Observable<any> {
      return this.http.get(BASE_URL + 'more/' + id + "/" + indexReview
      ) as Observable<any>;
    }

    deleteReview(id: number): Observable<any>{
      return this.http.delete(BASE_URL + id).pipe(
        catchError((error) => {
          return throwError(error);
        })
      ) as Observable<any>;;
    }

    isReviewed(idGame: number, idUser: number){
      return this.http.get(BASE_URL + 'review/' + idUser+ "/" + idGame) as Observable<any>;
    }

    reviewGame(idGame: number, idUser: number, formData: FormData){
      return this.http.post(BASE_URL + idGame+ "/" + idUser, formData).pipe(
        catchError((error) => {
          return throwError(error);
        })
      ) as Observable<any>;
    }
};
