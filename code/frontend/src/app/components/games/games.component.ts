import { GameService } from './../../services/game.service';
import { Component, OnInit } from '@angular/core';
import { Game } from 'src/app/models/game.model';
import { UserService } from 'src/app/services/user.service';
import { NgbRatingModule } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-games',
  templateUrl: './games.component.html',
  styleUrls: ['./games.component.css']
})
export class GamesComponent implements OnInit{

  games : Game[];
  moreGames : boolean = false;
  indexGames : number = 1;
  recomendedGames : Game[];

  images = [
    {
      src: 'https://via.placeholder.com/800x400',
      alt: 'First Image',
      title: 'First Image',
      description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.'
    },
    {
      src: 'https://via.placeholder.com/800x400',
      alt: 'Second Image',
      title: 'Second Image',
      description: 'Praesent quis nisi eget dui eleifend eleifend sit amet eget felis.'
    },
    {
      src: 'https://via.placeholder.com/800x400',
      alt: 'Third Image',
      title: 'Third Image',
      description: 'Suspendisse potenti. In faucibus diam diam, sit amet accumsan quam commodo vitae.'
    }
  ];

  constructor(private gameService: GameService, private userService: UserService) {

  }

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
