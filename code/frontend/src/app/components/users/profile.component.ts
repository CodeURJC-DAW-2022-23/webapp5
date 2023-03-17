import { LoginService } from './../../services/auth.service';
import { Router } from '@angular/router';
import { UserProfile } from './../../models/user.rest.model';
import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {

  constructor(private userService: UserService, private router: Router, private loginService: LoginService) { }

  userProfile: UserProfile;

  ngOnInit(): void {
    this.userService.getMe().subscribe((response) => {
      this.userProfile = response;
    });
  }

  getProfileImage(){
    return this.userService.getProfileImage(this.userProfile.user);
  }

  logout() {
    this.loginService.logOut();
    this.router.navigate(['/']);
  }

}
