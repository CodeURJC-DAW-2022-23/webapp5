import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Game } from 'src/app/models/game.model';
import { GameService } from 'src/app/services/game.service';

@Component({
  selector: 'app-add-game',
  templateUrl: './add-game.component.html'
})
export class AddGameComponent{
  category:string;
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

  constructor(private gameService: GameService, private router: Router) { }

  checkEmpty(input:string){
    if(input == undefined || input == ""){
      return false;
    }
    return true;
  }

  checkPrice() {
    return typeof this.price === 'number' && !Number.isNaN(this.price) && Number.isFinite(this.price) && this.price % 1 !== 0;;
  }

  onFilesSelected(event: any) {
    this.imageFields = event.target.files;
  }

  onFileSelected(event: any) {
    this.imageField = <File> event.target.files[0];
  }

  upload(event: any) {
    event.preventDefault();

    if (!this.checkEmpty(this.name) || !this.checkEmpty(this.category ) || !this.checkEmpty(this.category) || !this.checkEmpty(this.os) || !this.checkEmpty(this.processor) || !this.checkEmpty(this.memory) || !this.checkEmpty(this.directX) || !this.checkEmpty(this.network) || !this.checkEmpty(this.hardDrive) || !this.checkEmpty(this.soundcard) || !this.checkEmpty(this.graphics) || !this.checkEmpty(this.description)) {
      this.displayed = false;
      return;
    }

    if (!this.checkPrice()){
      this.displayed = false;
      return;
    }

    const formData = new FormData();
    formData.append('name', this.name);
    formData.append('category', this.category);
    formData.append('price', this.price.toString());
    formData.append('os', this.os);
    formData.append('processor', this.processor);
    formData.append('memory', this.memory);
    formData.append('directX', this.directX);
    formData.append('network', this.network);
    formData.append('hardDrive', this.hardDrive);
    formData.append('soundcard', this.soundcard);
    formData.append('graphics', this.graphics);
    formData.append('description', this.description);



    if (this.imageField != null) {
      formData.append('imageFile', this.imageField , this.imageField.name);
    }

    if (this.imageFields != null) {
      for (const element of this.imageFields) {
        formData.append('imageFiles', element, element.name);
      }
    }

    this.gameService.addGame(formData).subscribe((data) => {
      this.router.navigate(['/games']);
    });
  }
}
