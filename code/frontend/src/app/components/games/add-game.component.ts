import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-add-game',
  templateUrl: './add-game.component.html'
})
export class AddGameComponent implements OnInit {

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
  imageFields: File | null;
  description: string;
  displayed: boolean = true;

  constructor() { }

  ngOnInit(): void {
  }

  checkEmpty(input:string){
    if(input == undefined || input == ""){
      return false;
    }
    return true;
  }

  checkPrice() {
    throw new Error('Method not implemented.');
  }

  upload($event: any) {
    throw new Error('Method not implemented.');
    }
    onFilesSelected($event: any) {
    throw new Error('Method not implemented.');
    }
    onFileSelected($event: any) {
    throw new Error('Method not implemented.');
    }
}
