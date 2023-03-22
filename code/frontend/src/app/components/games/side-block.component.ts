import { GameService } from './../../services/game.service';
import { UserService } from './../../services/user.service';
import { Component, OnInit } from '@angular/core';
import { Game } from 'src/app/models/game.model';

@Component({
  selector: 'app-side-block',
  templateUrl: './side-block.component.html',
})
export class SideBlockComponent implements OnInit {

  recomendedGames : Game[];

  constructor(private userService: UserService, private gameService: GameService) {

  }

  ngOnInit(): void {
    this.userService.getRecomendations(5).subscribe((response) => {
      this.recomendedGames = response;
    });
  }

  gameImage(game: Game){
    return this.gameService.getGameCoverImage(game);
  }

}
