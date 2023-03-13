import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { BooksService } from '../../services/books.service';
import { Book } from '../../models/book.model';
import { LoginService } from 'src/app/services/login.service';

@Component({
    templateUrl: './book-detail.component.html'
})
export class BookDetailComponent {

    book: Book;

    constructor(private router: Router, activatedRoute: ActivatedRoute, public service: BooksService,
        public loginService: LoginService) {

        const id = activatedRoute.snapshot.params['id'];
        service.getBook(id).subscribe(
            book => this.book = book,
            error => console.error(error)
        );
    }

    removeBook() {
        const okResponse = window.confirm('Do you want to remove this book?');
        if (okResponse) {
            this.service.deleteBook(this.book).subscribe(
                _ => this.router.navigate(['/books']),
                error => console.error(error)
            );
        }
    }

    editBook() {
        this.router.navigate(['/books/edit', this.book.id]);
    }

    gotoBooks() {
        this.router.navigate(['/books']);
    }

    bookImage(){
        return this.book.image? '/api/books/'+this.book.id+'/image' : '/assets/images/no_image.png';
    }
}
