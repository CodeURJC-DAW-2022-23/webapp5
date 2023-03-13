import { Routes, RouterModule } from '@angular/router';

import { BookListComponent } from './components/books/book-list.component';
import { BookDetailComponent } from './components/books/book-detail.component';
import { BookFormComponent } from './components/books/book-form.component';

const appRoutes = [
    { path: 'books', component: BookListComponent },
    { path: 'books/new', component: BookFormComponent },
    { path: 'books/:id', component: BookDetailComponent },
    { path: 'books/edit/:id', component: BookFormComponent },
    { path: '', redirectTo: 'books', pathMatch: 'full' }
]

export const routing = RouterModule.forRoot(appRoutes);