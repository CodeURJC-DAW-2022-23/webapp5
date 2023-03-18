import { Routes, RouterModule, CanActivate } from '@angular/router';

import { AuthGuard } from './services/auth-guard.service';
import { RoleGuard as RoleGuard } from './services/role-guard.service';

import { BookListComponent } from './components/books/book-list.component';
import { BookDetailComponent } from './components/books/book-detail.component';
import { BookFormComponent } from './components/books/book-form.component';
import { LoginComponent } from './components/auth/login.component';
import { RegisterComponent } from './components/auth/register.component';
import { GamesComponent } from './components/games/games.component';
import { ProfileComponent } from './components/users/profile.component';
import { ErrorComponent } from './components/error/error.component';
import { EditUserComponent } from './components/users/edit-user.component';

const appRoutes = [
    { path: 'books', component: BookListComponent },
    { path: 'books/new', component: BookFormComponent, canActivate: [RoleGuard] },
    { path: 'books/:id', component: BookDetailComponent },
    { path: 'books/edit/:id', component: BookFormComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent},
    { path: '', component: GamesComponent },
    { path: 'profile/:id', component: ProfileComponent, canActivate: [AuthGuard]},
    { path: 'error/:id', component: ErrorComponent },
    { path: 'editProfile/:id', component: EditUserComponent, canActivate: [AuthGuard]},
    { path: '**', redirectTo: 'error/404' }
]

export const routing = RouterModule.forRoot(appRoutes);
