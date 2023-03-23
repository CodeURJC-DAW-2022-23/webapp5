import { UserService } from 'src/app/services/user.service';
import {
  Component,
  ElementRef,
  HostListener,
  Renderer2,
  ViewChild,
} from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../../services/auth.service';
import { GameService } from 'src/app/services/game.service';

@Component({
  selector: 'navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent {
  public isMenuCollapsed = true;

  public scrolled = false;

  @ViewChild('navbar', { read: ElementRef }) navbar: ElementRef;

  @HostListener('window:scroll', ['$event']) onScroll(event: any) {
    const scrollPosition =
      window.pageYOffset ||
      document.documentElement.scrollTop ||
      document.body.scrollTop ||
      0;
    // obtén la posición de desplazamiento de la ventana
    if (scrollPosition > 100) {
      // si el usuario ha desplazado más de 100px
      this.scrolled = true; // cambia la opacidad a 1
    }else{
      this.scrolled = false; // cambia la opacidad a 0.3
    } // actualiza el valor de color de fondo
  }

  constructor(
    public loginService: LoginService,
    private router: Router,
    private renderer: Renderer2,
    public userService: UserService,
    public gameService: GameService,
  ) {}

  ngOnInit(): void{
  }

  ngAfterViewInit() {
    // Obtenemos la altura de la navbar
    const navbarHeight = this.navbar.nativeElement.offsetHeight;
    // Establecemos el margen superior del contenido para que no colisione con la navbar
    const miDiv = document.querySelector('.mi-div');
    this.renderer.setStyle(miDiv, 'height', `${navbarHeight}px`);
  }

  login() {
    this.router.navigate(['/login']);
  }

  register() {
    this.router.navigate(['/register']);
  }

  myProfile(){
    this.router.navigate(['/profile/' + this.loginService.currentUser().id]);
  }

  currentUser() {
    return this.loginService.currentUser().name;
  }

  logout() {
    this.loginService.logOut();
    this.router.navigate(['/']);
  }

  totalPrice(){
    return this.loginService.currentUser().totalPrice;
  }

  deleteFromCart(id: number){
    this.userService.deleteFromCart(id, this.loginService.currentUser().id).subscribe(
      _ => {
        this.loginService.reqIsLogged();
      },
      error => {
        this.router.navigate(['/error/'+ error.status]);
      }
    );
  }

  controlPanel(){
    this.router.navigate(['/controlPanel']);
  }

  cart(){
    this.router.navigate(['/cart/' + this.loginService.currentUser().id]);
  }

  checkout(){
    this.router.navigate(['/checkout/' + this.loginService.currentUser().id]);
  }
}
