import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Book } from '../models/book.model';

const BASE_URL = '/api/books/';

@Injectable({ providedIn: 'root' })
export class BooksService {

	constructor(private httpClient: HttpClient) { }

	getBooks(): Observable<Book[]> {
		return this.httpClient.get(BASE_URL).pipe(
			catchError(error => this.handleError(error))
		) as Observable<Book[]>;
	}

	getBook(id: number | string): Observable<Book> {
		return this.httpClient.get(BASE_URL + id).pipe(
			catchError(error => this.handleError(error))
		) as Observable<Book>;
	}

	addBook(book: Book) {

		if (!book.id) {
			return this.httpClient.post(BASE_URL, book)
				.pipe(
					catchError(error => this.handleError(error))
				);
		} else {
			return this.httpClient.put(BASE_URL + book.id, book).pipe(
				catchError(error => this.handleError(error))
			);
		}
	}

	setBookImage(book: Book, formData: FormData) {
		return this.httpClient.post(BASE_URL + book.id + '/image', formData)
			.pipe(
				catchError(error => this.handleError(error))
			);
	}

	deleteBookImage(book: Book) {
		return this.httpClient.delete(BASE_URL + book.id + '/image')
			.pipe(
				catchError(error => this.handleError(error))
			);
	}

	deleteBook(book: Book) {
		return this.httpClient.delete(BASE_URL + book.id).pipe(
			catchError(error => this.handleError(error))
		);
	}

	updateBook(book: Book) {
		return this.httpClient.put(BASE_URL + book.id, book).pipe(
			catchError(error => this.handleError(error))
		);
	}

	private handleError(error: any) {
		console.log("ERROR:");
		console.error(error);
		return throwError("Server error (" + error.status + "): " + error.text())
	}
}