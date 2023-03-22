import { RoleGuard } from './services/role-guard.service';
import { RouterModule } from '@angular/router';

import { AuthGuard } from './services/auth-guard.service';

import { LoginComponent } from './components/auth/login.component';
import { RegisterComponent } from './components/auth/register.component';
import { GamesComponent } from './components/games/games.component';
import { ProfileComponent } from './components/users/profile.component';
import { ErrorComponent } from './components/error/error.component';
import { EditUserComponent } from './components/users/edit-user.component';
import { GamePageComponent } from './components/games/game-page.component';
import { ControlPanelComponent } from './components/games/control-panel.component';

const appRoutes = [
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent},
    { path: '', component: GamesComponent },
    { path: 'profile/:id', component: ProfileComponent, canActivate: [AuthGuard]},
    { path: 'error/:id', component: ErrorComponent },
    { path: 'editProfile/:id', component: EditUserComponent, canActivate: [AuthGuard]},
    { path: 'game/:id', component: GamePageComponent},
    { path: 'controlPanel', component: ControlPanelComponent, canActivate: [RoleGuard]},
    { path: '**', redirectTo: 'error/404' }
]

export const routing = RouterModule.forRoot(appRoutes);
