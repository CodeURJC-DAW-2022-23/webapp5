import { Component } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'login',
  templateUrl: './login.component.html'
})
export class LoginComponent {

  constructor(public loginService: LoginService, private router: Router) { }

  displayed: boolean = true;

  logIn(event: any, user: string, pass: string) {

    event.preventDefault();

    this.loginService.logIn(user, pass)
  .subscribe(
    (response) => {
      this.router.navigate(['/']);
    },
    (error) => {
      this.displayed = false;
    }
  );
  }

}
