import { Router } from '@angular/router';
import { GameService } from './../../services/game.service';
import { Component, OnInit } from '@angular/core';
import { Game } from 'src/app/models/game.model';

@Component({
  selector: 'app-control-panel',
  templateUrl: './control-panel.component.html',
})
export class ControlPanelComponent implements OnInit {
  constructor(public gameService: GameService, public router: Router) {}

  games: Game[] = [];
  moreGames: boolean = false;
  loading: boolean = false;
  indexGames: number = 1;

  ngOnInit(): void {
    this.gameService.getIndexGames().subscribe((response) => {
      this.games = response.content;
      this.moreGames = !response.last;
    });
  }

  deleteGame(id: number) {
    this.gameService.deleteGame(id).subscribe(
      (_) => {
        this.games = this.games.filter((game) => game.id !== id);
      },
      (error) => {
        this.router.navigate(['error/' + error.status]);
      }
    );
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

  goToGame(id: number) {
    this.router.navigate(['/game/' + id]);
  }

  goToEditGame(id: number) {
    this.router.navigate(['/editGame/' + id]);
  }
}
