import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { BookDetailComponent } from './components/books/book-detail.component';
import { BookListComponent } from './components/books/book-list.component';
import { BookFormComponent } from './components/books/book-form.component';
import { routing } from './app.routing';
import { LoginComponent } from './components/login/login.component';

@NgModule({
  declarations: [AppComponent, BookDetailComponent, BookListComponent, BookFormComponent, LoginComponent],
  imports: [BrowserModule, FormsModule, HttpClientModule, routing],
  bootstrap: [AppComponent]
})
export class AppModule { }
