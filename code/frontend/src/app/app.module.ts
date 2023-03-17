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

@NgModule({
  declarations: [AppComponent, BookDetailComponent, BookListComponent, BookFormComponent, LoginComponent, NavbarComponent, RegisterComponent, AppFooterComponent, GamesComponent, SideBlockComponent],
  imports: [BrowserModule, FormsModule, HttpClientModule, routing, NgbModule],
  bootstrap: [AppComponent]
})
export class AppModule { }
