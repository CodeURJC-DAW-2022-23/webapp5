import { GameService } from './../../services/game.service';
import { UserService } from './../../services/user.service';
import { Component, OnInit } from '@angular/core';
import { Game } from 'src/app/models/game.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-side-block',
  templateUrl: './side-block.component.html',
})
export class SideBlockComponent implements OnInit {

  recomendedGames : Game[];

  constructor(private userService: UserService, private gameService: GameService, private router: Router) {

  }

  ngOnInit(): void {
    this.userService.getRecomendations(5).subscribe((response) => {
      this.recomendedGames = response;
    });
  }

  gameImage(game: Game){
    return this.gameService.getGameCoverImage(game);
  }

  searchName(name: string){
    this.router.navigate(['/search'], { queryParams: { name: name } });
  }

  goToGame(id: number) {
    this.router.navigate(['/game/' + id]);
  }

  searchCategory(category: string){
    this.router.navigate(['/search'], { queryParams: { category: category } });
  }

}
