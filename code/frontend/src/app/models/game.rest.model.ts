import { Review } from './review.model';
import { Game } from './game.model';
export interface UserProfile {
    game: Game;
    reviews: Review[];
}
