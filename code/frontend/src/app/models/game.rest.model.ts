import { Review } from './review.model';
import { Game } from './game.model';
export interface GameInfo {
  game: Game;
  reviews: Review[];
}
