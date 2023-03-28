import { LoginService } from './../../services/auth.service';
import { GameService } from './../../services/game.service';
import { UserService } from './../../services/user.service';
import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/user.model';
import { ActivatedRoute, Router } from '@angular/router';
import { Game } from 'src/app/models/game.model';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
})
export class CartComponent implements OnInit {

  constructor(public userService: UserService, public activatedRoute: ActivatedRoute, public authService: LoginService, public router: Router, public gameService: GameService) { }

  user: User;
  cart: Game[];
  moreGames: boolean =  false;
  loading: boolean = false;
  indexGames: number = 1;

  ngOnInit(): void {
    this.userService.viewCart(this.activatedRoute.snapshot.params['id']).subscribe((response) => {
      this.cart = response.content;
      this.moreGames = !response.last;
    },
    (error) => {
      this.router.navigate(['error/'+ error.status]);
    }
    );
    this.userService.getMe().subscribe((response) => {
      this.user = response.user;
    }
    );
  }

  deleteCartGame(id: number){
    this.userService.deleteFromCart(id, this.user.id).subscribe((response) => {
      this.ngOnInit();
      this.authService.reqIsLogged();
    },
    (error) => {
      this.router.navigate(['error/'+ error.status]);
    }
    );
  }

  gameImage(game: Game) {
    return this.gameService.getGameCoverImage(game);
  }

  getMoreGames() {
    this.loading = true; // establece loading a true antes de hacer la solicitud
    this.userService.getMoreCartGames(this.user.id, this.indexGames).subscribe((response) => {
      this.cart = this.cart.concat(response.content);
      this.moreGames = !response.last;
      this.indexGames++;
      this.loading = false; // establece loading a false después de recibir la respuesta
    },
    (error) => {
      this.router.navigate(['error/'+ error.status]);
    }
    );
  }

  goToGame(id: number) {
    this.router.navigate(['/game/' + id]);
  }

  goToCheckout(){
    this.router.navigate(['/checkout/'+ this.user.id]);
  }

}
