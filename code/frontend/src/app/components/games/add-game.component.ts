import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { GameService } from 'src/app/services/game.service';

@Component({
  selector: 'app-add-game',
  templateUrl: './add-game.component.html',
})
export class AddGameComponent {
  category: string;
  name: string;
  price: number;
  os: string;
  processor: string;
  memory: string;
  directX: string;
  network: string;
  hardDrive: string;
  soundcard: string;
  graphics: string;
  imageField: File = null;
  imageFields: File[];
  description: string;
  displayed: boolean = true;

  constructor(private gameService: GameService, public router: Router) {}

  checkEmpty(input: string) {
    if (input == undefined || input == '') {
      return false;
    }
    return true;
  }

  checkPrice() {
    if (
      typeof this.price === 'number' &&
      !Number.isNaN(this.price) &&
      Number.isFinite(this.price) &&
      this.price % 1 !== 0
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

  upload(event: any) {
    event.preventDefault();

    if (
      !this.checkEmpty(this.name) ||
      !this.checkEmpty(this.category) ||
      !this.checkEmpty(this.os) ||
      !this.checkEmpty(this.processor) ||
      !this.checkEmpty(this.memory) ||
      !this.checkEmpty(this.directX) ||
      !this.checkEmpty(this.network) ||
      !this.checkEmpty(this.hardDrive) ||
      !this.checkEmpty(this.soundcard) ||
      !this.checkEmpty(this.graphics) ||
      !this.checkEmpty(this.description)
    ) {
      this.displayed = false;
      return;
    }

    if (this.checkPrice() == 'Price must be a number') {
      this.displayed = false;
      return;
    }

    if (this.imageField == null) {
      this.displayed = false;
      return;
    }

    if (this.imageFields == null || this.imageFields.length == 0) {
      this.displayed = false;
      return;
    }

    this.gameService
      .addGame(
        this.name,
        this.category,
        this.price,
        this.os,
        this.processor,
        this.memory,
        this.directX,
        this.network,
        this.hardDrive,
        this.soundcard,
        this.graphics,
        this.imageField,
        this.imageFields,
        this.description
      )
      .subscribe(
        (response) => {
          this.router.navigate(['']);
        },
        (error) => {
          this.router.navigate(['error/' + error.status]);
        }
      );
  }
}
