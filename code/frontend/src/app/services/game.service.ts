import { Game } from './../models/game.model';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

const BASE_URL = '/api/games/';

@Injectable({ providedIn: 'root' })
export class GameService {

	constructor(private http: HttpClient) { }

    getIndexGames(): Observable<any> {
    return this.http.get(BASE_URL
    ) as Observable<any>;}

    getGameCoverImage(game: Game) {
      return game.titleImage ? BASE_URL + game.id + '/coverImage' : '/assets/images/no_image.png';
    }

    getGameplayImage(game: Game, index: number) {
      return game.gameplayImages[index - 1] ? BASE_URL + game.id + '/gameplayImage/' + index : '/assets/images/no_image.png';
    }

    getMoreGames(indexGames: number): Observable<any> {
      return this.http.get(BASE_URL + 'moreIndexGames/' + indexGames
      ) as Observable<any>;
    }

    getGameById(id: number): Observable<any> {
      return this.http.get(BASE_URL  + id).pipe(
        catchError((error) => {
          return throwError(error);
        })
      ) as Observable<any>;
    }

    deleteGame(id: number): Observable<any> {
      return this.http.delete(BASE_URL + id).pipe(
        catchError((error) => {
          return throwError(error);
        })
      ) as Observable<any>;
    }

    searchGames(category: String, name: String): Observable<any> {
      return this.http.get(BASE_URL + 'search/?name=' + name + '&category=' + category).pipe(
        catchError((error) => {
          return throwError(error);
        })
      ) as Observable<any>;
    }

    moreFoundGames(category: String, name: String, indexGames: number): Observable<any> {
      return this.http.get(BASE_URL + 'moreFoundGames/' + indexGames + '/?name=' + name + '&category=' + category).pipe(
        catchError((error) => {
          return throwError(error);
        })
      ) as Observable<any>;
    }

    addGame(name: string, category: string, price: number, os: string, processor: string, memory: string, directX: string, network: string, hardDrive: string, soundcard: string, graphics: string, imageField: File, imageFields: File[], description: string) {
      const formData = new FormData();
      formData.append('name', name);
      formData.append('category', category);
      formData.append('price', price.toString());
      formData.append('os', os);
      formData.append('processor', processor);
      formData.append('memory', memory);
      formData.append('directX', directX);
      formData.append('network', network);
      formData.append('hardDrive', hardDrive);
      formData.append('soundcard', soundcard);
      formData.append('graphics', graphics);
      formData.append('imageField', imageField);
      for (let i = 0; i < imageFields.length; i++) {
        formData.append('imageFields', imageFields[i]);
      }
      formData.append('description', description);
      return this.http.post(BASE_URL, formData).pipe( map((response: any) => {
        return response;
      }),
      catchError((error: any) => {
        return throwError('Something went wrong');
      })
      ) as Observable<any>;
    }

    editGame(game: Game, imageField: File, imageFields: File[]) {
      const formData = new FormData();
      formData.append('name', game.name);
      formData.append('category', game.category);
      formData.append('price', game.price.toString());
      formData.append('os', game.os);
      formData.append('processor', game.processor);
      formData.append('memory', game.memory);
      formData.append('directX', game.directX);
      formData.append('network', game.network);
      formData.append('hardDrive', game.hardDrive);
      formData.append('soundcard', game.soundCard);
      formData.append('graphics', game.graphics);
      formData.append('description', game.description);
      if(imageField){
        formData.append('imageField', imageField);
      }
      if(imageFields.length > 0){
        for (let i = 0; i < imageFields.length; i++) {
          formData.append('imageFields', imageFields[i]);
        }
      }
      return this.http.put(BASE_URL + game.id, formData).pipe( map((response: any) => {
        return response;
      }),
      catchError((error: any) => {
        return throwError('Something went wrong');
      })
      ) as Observable<any>;
    }
}
