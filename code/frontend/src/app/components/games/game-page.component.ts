import { ReviewService } from './../../services/review.service';
import { Review } from './../../models/review.model';
import { LoginService } from './../../services/auth.service';
import { GameService } from './../../services/game.service';
import { GameInfo } from './../../models/game.rest.model';
import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { UserProfile } from 'src/app/models/user.rest.model';
import { User } from 'src/app/models/user.model';

@Component({
  selector: 'app-game-page',
  templateUrl: './game-page.component.html',
  styleUrls: ['./games.component.css'],
})
export class GamePageComponent implements OnInit {

  gameInfo: GameInfo;
  userProfile: UserProfile;
  moreReviews: boolean = false;
  indexReviews: number = 1;
  loading: boolean = false;
  isReviewed : boolean = true;
  comment: string = "";
  reviewRate: number = 1;
  inCart: boolean;

  images: Array<object> = [];

  constructor(public authService: LoginService, private router: Router,
    public activatedRoute: ActivatedRoute, public gameService: GameService, public userService: UserService,
    public reviewService: ReviewService) { }

  ngOnInit(): void {
    this.userService.getMe().subscribe(
      (response) => {
        this.userProfile = response;
      }
    );

    this.loadMainGame();
  }

  loadMainGame(){
    this.gameService.getGameById(this.activatedRoute.snapshot.params['id']).subscribe(
      (response) => {
        this.gameInfo = response;
        this.moreReviews = !response.reviews.last;
        this.gameInfo.reviews = response.reviews.content;
        this.gameplayImages().forEach(element => {
          this.images.push({image: element, thumbImage: element});
        });
        this.reviewService.isReviewed(this.gameInfo.game.id, this.userProfile.user.id).subscribe(
          (response) => {
            this.isReviewed = response;
          }
        );
        this.inCart = this.gameInCart();
      },
      (error) => {
        this.router.navigate(['error/'+ error.status]);
      }
    );
  }

  isAdmin(){
    return this.authService.isAdmin();
  }

  gameplayImages(){
    let index: number = 0;
    let images: string[] = [];
    this.gameInfo.game.gameplayImages.forEach(element => {
      images.push(this.gameService.getGameplayImage(this.gameInfo.game, index + 1));
      index++;
    });
    return images;
  }

  getMoreReviews(){
    this.loading = true; // establece loading a true antes de hacer la solicitud
    this.reviewService.getMoreReviews(this.gameInfo.game.id, this.indexReviews).subscribe((response) => {
      this.gameInfo.reviews = this.gameInfo.reviews.concat(response.content);
      this.moreReviews = !response.last;
      this.indexReviews++;
      this.loading = false; // establece loading a false después de recibir la respuesta
    });
  }

  deleteReview(id:number){
    this.reviewService.deleteReview(id).subscribe(
      (response) => {
        this.loadMainGame();
      },
      (error) => {
        this.router.navigate(['error/'+ error.status]);
      }
    );
  }

  gameInCart(){
    return this.userProfile.user.cart.some(game => game.id === this.gameInfo.game.id);
  }

  gameBought(){
    return this.userProfile.games.some(game => game.id === this.gameInfo.game.id);
  }

  setRate(rate: number){
    this.reviewRate = rate;
  }

  reviewGame(){
    const formData = new FormData();
    formData.append('comment', this.comment);
    formData.append('reviewRate', this.reviewRate.toString());
    this.reviewService.reviewGame(this.gameInfo.game.id, this.userProfile.user.id, formData).subscribe(
      (response) => {
        this.loadMainGame();
      },
      (error) => {
        this.router.navigate(['error/'+ error.status]);
      }
    );
  }

  checkIfDelete(user: UserProfile, review: Review){
    if (!this.authService.isLogged()){
      return false;
    }
    if (this.authService.isAdmin() || this.userProfile.user.id === review.user.id){
      return true;
    }
    return false;
  }

  addToCart(event: any){
    event.preventDefault();
    this.userService.addToCart(this.gameInfo.game.id, this.userProfile.user.id).subscribe(
      (response) => {
        this.userProfile.user.cart = this.userProfile.user.cart.concat(response.content);
        this.inCart = true;
        this.authService.reqIsLogged();
      },
      (error) => {
        this.router.navigate(['error/'+ error.status]);
      }
    );
  }

  goToEdit(){
    this.router.navigate(['editGame/'+ this.gameInfo.game.id]);
  }
}
