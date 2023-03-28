import { Game } from "./game.model";

export interface User {
  id?: number;
  mail: string;
  encodedPassword: string;
  name: string;
  lastName: string;
  aboutMe: string;
  billingInformation: string;
  roles: string[];
  cart: Game[];
  profilePircture: string;
  totalPrice: number;
}
