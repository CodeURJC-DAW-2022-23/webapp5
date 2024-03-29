import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Game } from 'src/app/models/game.model';
import { GameService } from 'src/app/services/game.service';

@Component({
  selector: 'app-edit-game',
  templateUrl: './edit-game.component.html',
})
export class EditGameComponent implements OnInit {
  game: Game;
  displayed: boolean = true;
  imageField: File = null;
  imageFields: File[];

  constructor(private gameService: GameService, private router: Router) {}

  ngOnInit(): void {
    this.gameService.getGameById(+this.router.url.split('/')[2]).subscribe(
      (response) => {
        this.game = response.game;
      },
      (_) => {
        this.router.navigate(['error/403']);
      }
    );
  }

  checkEmpty(input: string) {
    if (input == undefined || input == '') {
      return false;
    }
    return true;
  }

  checkPrice() {
    if (
      typeof this.game.price === 'number' &&
      !Number.isNaN(this.game.price) &&
      Number.isFinite(this.game.price) &&
      this.game.price % 1 !== 0
    ) {
      return;
    } else {
      return 'Price must be a number';
    }
  }

  onFilesSelected(event: any) {
    if (event.target.files.length > 0) {
      this.imageFields = event.target.files;
    }
  }

  onFileSelected(event: any) {
    this.imageField = event.target.files[0];
  }

  edit(event: any) {
    event.preventDefault();

    if (
      !this.checkEmpty(this.game.name) ||
      !this.checkEmpty(this.game.category) ||
      !this.checkEmpty(this.game.os) ||
      !this.checkEmpty(this.game.processor) ||
      !this.checkEmpty(this.game.memory) ||
      !this.checkEmpty(this.game.directX) ||
      !this.checkEmpty(this.game.network) ||
      !this.checkEmpty(this.game.hardDrive) ||
      !this.checkEmpty(this.game.soundCard) ||
      !this.checkEmpty(this.game.graphics) ||
      !this.checkEmpty(this.game.description)
    ) {
      this.displayed = false;
      return;
    }

    if (this.checkPrice() == 'Price must be a number') {
      this.displayed = false;
      return;
    }

    this.gameService
      .editGame(this.game, this.imageField, this.imageFields)
      .subscribe(
        (_) => {
          this.goToGame();
        },
        (error) => {
          this.router.navigate(['error/' + error.status]);
        }
      );
  }

  goToGame() {
    this.router.navigate(['/game/' + this.game.id]);
  }
}
