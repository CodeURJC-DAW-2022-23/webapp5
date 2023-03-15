import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/auth.service';
import { Book } from './../../models/book.model';
import { BooksService } from './../../services/books.service';


@Component({
  templateUrl: './book-list.component.html'
})
export class BookListComponent implements OnInit {

  books: Book[];

  constructor(private router: Router, private service: BooksService, public loginService: LoginService) { }

  ngOnInit() {
    this.service.getBooks().subscribe(
      books => this.books = books,
      error => console.log(error)
    );
  }

  newBook() {
    this.router.navigate(['/books/new']);
  }
}
