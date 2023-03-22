import { Game } from "./game.model";
import { User } from "./user.model";

export interface Review {
  id?: number;
  user: User;
  game: Game;
  date: Date;
  rating: number;
  comment: string;
}
