import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html'
})
export class ErrorComponent {

  id: string;
  message: Map<string, string> = new Map([
    ["403", "Forbidden"],
    ["404", "Not Found"],
    ["500", "Internal Server Error"]
  ]);

  constructor(private router: Router,
    activatedRoute: ActivatedRoute) {
    this.id = activatedRoute.snapshot.params['id'];
  }

  mainPage(){
    this.router.navigate(['']);
  }

}
