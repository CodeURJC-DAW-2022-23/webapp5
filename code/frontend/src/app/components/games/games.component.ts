import { GameService } from './../../services/game.service';
import { Component, OnInit } from '@angular/core';
import { Game } from 'src/app/models/game.model';

@Component({
  selector: 'app-games',
  templateUrl: './games.component.html',
  styleUrls: ['./games.component.css']
})
export class GamesComponent implements OnInit{

  games : Game[];
  moreGames : boolean = false;
  indexGames : number = 1;

  constructor(private gameService: GameService) {

  }

  ngOnInit(): void {
    this.gameService.getIndexGames().subscribe((response) => {
      this.games = response.content;
      this.moreGames = !response.last;
      // Aquí puedes trabajar con la información obtenida
    });
  }

  gameImage(game: Game){
    return this.gameService.getGameCoverImage(game);
  }

  getMoreGames(){
    this.gameService.getMoreGames(this.indexGames).subscribe((response) => {
      this.games = this.games.concat(response.content);
      this.moreGames = !response.last;
      this.indexGames++;
    });
  }
}
