import { LoginService } from './../../services/auth.service';
import { Router, ActivatedRoute} from '@angular/router';
import { UserProfile } from './../../models/user.rest.model';
import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {

  constructor(private userService: UserService, private router: Router, private loginService: LoginService,
   public activatedRoute: ActivatedRoute) { }

  userProfile: UserProfile;

  ngOnInit(): void {
    this.userService.getMe().subscribe((response) => {
      this.userProfile = response;
      if (this.userProfile.user.id.toString() != this.activatedRoute.snapshot.params['id']) {
        this.router.navigate(['error/403']);
      }
    });
  }

  getProfileImage(){
    return this.userService.getProfileImage(this.userProfile.user);
  }

  logout() {
    this.loginService.logOut();
    this.router.navigate(['/']);
  }

  goToGame(id: number) {
    this.router.navigate(['/game/' + id]);
  }

  editProfile(){
    this.router.navigate(['/editProfile/' + this.userProfile.user.id]);
  }

}
