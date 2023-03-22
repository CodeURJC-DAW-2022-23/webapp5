import { GameService } from './../../services/game.service';
import { Component, OnInit } from '@angular/core';
import { Game } from 'src/app/models/game.model';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-games',
  templateUrl: './games.component.html',
  styleUrls: ['./games.component.css'],
})
export class GamesComponent implements OnInit {
  games: Game[];
  moreGames: boolean = false;
  indexGames: number = 1;
  recomendedGames: Game[];
  loading: boolean = false;

  constructor(
    private gameService: GameService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.gameService.getIndexGames().subscribe((response) => {
      this.games = response.content;
      this.moreGames = !response.last;
      // Aquí puedes trabajar con la información obtenida
    });
    this.userService.getRecomendations(3).subscribe((response) => {
      this.recomendedGames = response;
    });
  }

  gameImage(game: Game) {
    return this.gameService.getGameCoverImage(game);
  }

  getMoreGames() {
    this.loading = true; // establece loading a true antes de hacer la solicitud
    this.gameService.getMoreGames(this.indexGames).subscribe((response) => {
      this.games = this.games.concat(response.content);
      this.moreGames = !response.last;
      this.indexGames++;
      this.loading = false; // establece loading a false después de recibir la respuesta
    });
  }
}
