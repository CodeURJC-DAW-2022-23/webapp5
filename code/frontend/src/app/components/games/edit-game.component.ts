import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Game } from 'src/app/models/game.model';
import { GameService } from 'src/app/services/game.service';

@Component({
  selector: 'app-edit-game',
  templateUrl: './edit-game.component.html'
})
export class EditGameComponent implements OnInit {
  game: Game;
  displayed: boolean = true;

  constructor(private gameService: GameService, private router: Router) { }

  ngOnInit(): void {
    this.gameService.getGameById(+this.router.url.split('/')[2]).subscribe((response) => {
      this.game = response;
    });
  }

  checkEmpty(input:string){
    if(input == undefined || input == ""){
      return false;
    }
    return true;
  }

  checkPrice() {
   if (typeof this.game.price === 'number' && !Number.isNaN(this.game.price) && Number.isFinite(this.game.price) && this.game.price % 1 !== 0){
      return;
   }else{
      return "Price must be a number";
   }
  }

  onFilesSelected(event: any) {
    if (event.target.files.length > 0) {
      this.game.gameplayImages = event.target.files;
    }
  }

  onFileSelected(event: any) {
    this.game.titleImage = event.target.files[0];
  }

  upload(event: any) {
    event.preventDefault();

    if (!this.checkEmpty(this.game.name) || !this.checkEmpty(this.game.category ) || !this.checkEmpty(this.game.os) || !this.checkEmpty(this.game.processor) || !this.checkEmpty(this.game.memory) || !this.checkEmpty(this.game.directX) || !this.checkEmpty(this.game.network) || !this.checkEmpty(this.game.hardDrive) || !this.checkEmpty(this.game.soundCard) || !this.checkEmpty(this.game.graphics) || !this.checkEmpty(this.game.description)) {
      this.displayed = false;
      return;
    }

    if (this.checkPrice() == "Price must be a number"){
      this.displayed = false;
      return;
    }

    if (this.game.titleImage == null){
      this.displayed = false;
      return;
    }

    if (this.game.gameplayImages == null || this.game.gameplayImages.length == 0){
      this.displayed = false;
      return;
    }

    this.gameService.editGame(this.game).subscribe((response) => {
      this.router.navigate(['/game/' + this.game.id]);
    },
    error => {
      this.displayed = false;
    });
  }
}
