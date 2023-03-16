import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../../services/auth.service';

@Component({
  selector: 'navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent{
  public isMenuCollapsed = true;


  constructor(public loginService: LoginService, private router: Router) { }

  login(){
    this.router.navigate(['/login']);
  }

  register(){
    this.router.navigate(['/register']);
  }

  currentUser(){
    return this.loginService.currentUser().name;
  }

  logout(){
    this.loginService.logOut();
    this.router.navigate(['/']);
  }

}
