import { Game } from './game.model';
import { User } from './user.model';
export interface UserProfile {
    user: User;
    games: Game[];
}
