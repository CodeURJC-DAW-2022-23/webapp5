import { LoginService } from 'src/app/services/auth.service';
import { Component, OnInit} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { UserProfile } from 'src/app/models/user.rest.model';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
})
export class EditUserComponent implements OnInit {

  constructor(private userService: UserService, private router: Router,
    public activatedRoute: ActivatedRoute, public authService: LoginService) { }

   userProfile: UserProfile;
   rPass: string ="";
   newPass: string = "";
   displayed: boolean = true;
   selectedFile: File = null;


   ngOnInit(): void {
     this.userService.getMe().subscribe((response) => {
       this.userProfile = response;
       if (this.userProfile.user.id.toString() != this.activatedRoute.snapshot.params['id']) {
         this.router.navigate(['error/403']);
       }
     });
   }
   checkEmpty(input:string){
    if(input == undefined || input == ""){
      return false;
    }
    return true;
  }

  checkPassword(){
    if (this.newPass.length > 0 && this.newPass.length < 8){
      return "The password must be at least 8 characters long";
    }
    if (this.newPass != this.rPass){
      return "The passwords doesn't match";
    }
    return "";
  }

  onFileSelected(event) {
    this.selectedFile = <File>event.target.files[0];
  }

  editUser(event: any){

    event.preventDefault();

    if (!this.checkEmpty(this.userProfile.user.name) || !this.checkEmpty(this.userProfile.user.lastName)){
      this.displayed = false;
      return;
    }

    if (this.checkPassword() != ""){
      this.displayed = false;
      return;
    }

    const formData = new FormData();
    formData.append('name', this.userProfile.user.name);
    formData.append('lastName', this.userProfile.user.lastName);
    formData.append('aboutMe', this.userProfile.user.aboutMe);

    if (this.selectedFile) {
      formData.append('imageFile', this.selectedFile, this.selectedFile.name);
    }

    this.userService.editUser(this.userProfile.user.id, formData).subscribe(
      response => {
        this.authService.reqIsLogged();
        this.router.navigate(['/profile/' + this.userProfile.user.id]);
      },
      error => {
        this.router.navigate(['error/'+ error.status]);
      }
    );
  }

}
