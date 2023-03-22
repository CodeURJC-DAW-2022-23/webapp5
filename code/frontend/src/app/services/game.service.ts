import { User } from './../models/user.model';
import { Game } from './../models/game.model';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

const BASE_URL = '/api/games/';

@Injectable({ providedIn: 'root' })
export class GameService {


	constructor(private http: HttpClient) { }

  getIndexGames(): Observable<any> {
    return this.http.get(BASE_URL + '/'
    ) as Observable<any>;}

    getGameCoverImage(game: Game) {
      return game.titleImage ? '/api/games/' + game.id + '/coverImage' : '/assets/images/no_image.png';
    }

    getGameplayImage(game: Game, index: number) {
      return game.gameplayImages[index - 1] ? BASE_URL + game.id + '/gameplayImage/' + index : '/assets/images/no_image.png';
    }

    getMoreGames(indexGames: number): Observable<any> {
      return this.http.get(BASE_URL + '/moreIndexGames/' + indexGames
      ) as Observable<any>;
    }

    getGameById(id: number): Observable<any> {
      return this.http.get(BASE_URL + '/' + id).pipe(
        catchError((error) => {
          return throwError(error);
        })
      ) as Observable<any>;
    }


};
