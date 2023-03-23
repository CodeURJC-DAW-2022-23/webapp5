import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { NgbModule} from '@ng-bootstrap/ng-bootstrap';

import { AppComponent } from './app.component';
import { BookDetailComponent } from './components/books/book-detail.component';
import { BookListComponent } from './components/books/book-list.component';
import { BookFormComponent } from './components/books/book-form.component';
import { routing } from './app.routing';
import { LoginComponent } from './components/auth/login.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { RegisterComponent } from './components/auth/register.component';
import { AppFooterComponent } from './components/app-footer/app-footer.component';
import { GamesComponent } from './components/games/games.component';
import { SideBlockComponent } from './components/games/side-block.component';
import { ProfileComponent } from './components/users/profile.component';
import { ErrorComponent } from './components/error/error.component';
import { EditUserComponent } from './components/users/edit-user.component';
import { GamePageComponent } from './components/games/game-page.component';
import { NgImageSliderModule } from 'ng-image-slider';
import { ControlPanelComponent } from './components/games/control-panel.component';
import { CartComponent } from './components/users/cart.component';
import { CheckoutComponent } from './components/users/checkout.component';
import { SearchComponent } from './components/games/search.component';

@NgModule({
  declarations: [AppComponent, BookDetailComponent, BookListComponent, BookFormComponent, LoginComponent, NavbarComponent, RegisterComponent, AppFooterComponent, GamesComponent, SideBlockComponent, ProfileComponent, ErrorComponent, EditUserComponent, GamePageComponent, ControlPanelComponent, CartComponent, CheckoutComponent, SearchComponent],
  imports: [BrowserModule, FormsModule, HttpClientModule, routing, NgbModule, NgImageSliderModule],
  bootstrap: [AppComponent]
})
export class AppModule { }
