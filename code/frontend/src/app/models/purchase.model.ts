import { Game } from "./game.model";
import { User } from "./user.model";

export interface Purchase {
  id?: number;
  games: Game[];
  user: User;
  date: Date;
}
