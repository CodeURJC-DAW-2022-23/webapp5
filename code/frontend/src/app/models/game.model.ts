export interface Game {
  id?: number;
  name: string;
  description: string;
  price: number;
  category: string;
  os: string;
  processor: string;
  memory: string;
  graphics: string;
  directX: string;
  network: string;
  hardDrive: string;
  soundCard: string;
  deleted: boolean;
  totalRating: number;
  starDistribution: number[];
  gameplayImages: string[];
  titleImage: string;
  reviewCount: number;
  starDistributionInt: number[];
}
