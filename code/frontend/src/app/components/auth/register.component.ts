import { Component, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { encode } from 'querystring';
import { LoginService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html'
})
export class RegisterComponent{

  name:string;
  lastName: string;
  encodedPassword:string = "";
  email:string;
  aboutMe: string;
  rPassword: string;
  rEmail: String;
  displayed: boolean = true;
  displayedRegister: boolean = true;

  constructor(public LoginService: LoginService, private router: Router) { }

  register(event: any, name: string, lastName: string){

    event.preventDefault();

    if (!this.checkEmpty(name) || !this.checkEmpty(lastName)){
      this.displayed = false;
      return;
    }
    if (this.checkEmail() != "" || this.checkPassword() != ""){
      this.displayed = false;
      return;
    }

    this.LoginService.register(name, lastName, this.email, this.encodedPassword, this.aboutMe).subscribe(
      response => {
        this.router.navigate(['/login']);
      },
      error => {
        this.displayedRegister = false;
      }
    );
  }

  checkEmpty(input:string){
    if(input == undefined || input == ""){
      return false;
    }
    return true;
  }

  checkEmail(){
    if (!this.checkEmpty(this.email)){
      return "The email can't be empty";
    }
    if (! this.email.toLowerCase()
    .match(
      /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    )){
      return "The email is not valid";
    }
    if (this.email != this.rEmail){
      return "The emails don't match";
    }
    return "";
  }

  checkPassword(){
    if (!this.checkEmpty(this.encodedPassword) || this.encodedPassword.length < 8){
      return "The password must be at least 8 characters long";
    }
    if (this.encodedPassword != this.rPassword){
      return "The passwords doesn't match";
    }
    return "";
  }





}
