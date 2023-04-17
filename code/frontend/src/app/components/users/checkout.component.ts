import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Game } from 'src/app/models/game.model';
import { User } from 'src/app/models/user.model';
import { LoginService } from 'src/app/services/auth.service';
import { GameService } from 'src/app/services/game.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
})
export class CheckoutComponent implements OnInit {
  constructor(
    public userService: UserService,
    public activatedRoute: ActivatedRoute,
    public authService: LoginService,
    public router: Router,
    public gameService: GameService
  ) {}

  user: User;
  cart: Game[];
  moreGames: boolean = false;
  loading: boolean = false;
  indexGames: number = 1;
  country: string = '';
  street: string = '';
  apartment: string = '';
  city: string = '';
  postCode: string = '';
  phone: string = '';
  checkoutLoading = false;
  displayed = true;

  ngOnInit(): void {
    this.userService
      .viewCart(this.activatedRoute.snapshot.params['id'])
      .subscribe(
        (response) => {
          this.cart = response.content;
          this.moreGames = !response.last;
        },
        (error) => {
          this.router.navigate(['error/' + error.status]);
        }
      );
    this.userService.getMe().subscribe((response) => {
      this.user = response.user;
      console.log(this.user);
    });
  }

  deleteCartGame(id: number) {
    this.userService.deleteFromCart(id, this.user.id).subscribe(
      (response) => {
        this.ngOnInit();
        this.authService.reqIsLogged();
      },
      (error) => {
        this.router.navigate(['error/' + error.status]);
      }
    );
  }

  gameImage(game: Game) {
    return this.gameService.getGameCoverImage(game);
  }

  getMoreGames() {
    this.loading = true; // establece loading a true antes de hacer la solicitud
    this.userService.getMoreCartGames(this.user.id, this.indexGames).subscribe(
      (response) => {
        this.cart = this.cart.concat(response.content);
        this.moreGames = !response.last;
        this.indexGames++;
        this.loading = false; // establece loading a false después de recibir la respuesta
      },
      (error) => {
        this.router.navigate(['error/' + error.status]);
      }
    );
  }

  checkEmpty(input: string) {
    if (input == undefined || input == '') {
      return false;
    }
    return true;
  }

  checkOut(event: any) {
    event.preventDefault();
    this.checkoutLoading = true;
    if (
      (this.checkEmpty(this.country) &&
        this.checkEmpty(this.street) &&
        this.checkEmpty(this.apartment) &&
        this.checkEmpty(this.city) &&
        this.checkEmpty(this.postCode) &&
        this.checkEmpty(this.phone)) ||
      this.checkEmpty(this.user.billingInformation)
    ) {
      const formData = new FormData();
      formData.append('billing_country', this.country);
      formData.append('billing_street', this.street);
      formData.append('billing_apartment', this.apartment);
      formData.append('billing_city', this.city);
      formData.append('billing_postcode', this.postCode);
      formData.append('billing_phone', this.phone);
      console.log(formData);

      this.userService.checkOut(this.user.id, formData).subscribe(
        (response) => {
          this.authService.reqIsLogged();
          this.router.navigate(['/']);
        },
        (error) => {
          this.router.navigate(['error/' + error.status]);
        }
      );
    } else {
      this.displayed = false;
      this.checkoutLoading = false;
    }
  }
}
