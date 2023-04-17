import { GameService } from 'src/app/services/game.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Game } from 'src/app/models/game.model';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
})
export class SearchComponent implements OnInit {
  name: string;
  category: string;
  games: Game[];
  moreGames: boolean = false;
  indexGames: number = 1;
  loading: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    public gameService: GameService
  ) {}

  ngOnInit(): void {
    this.name = this.route.snapshot.queryParamMap.get('name');
    if (this.name == null) {
      this.name = '';
    }
    this.category = this.route.snapshot.queryParamMap.get('category');
    if (this.category == null) {
      this.category = '';
    }

    this.router.navigate([], {
      queryParams: {
        name: null,
        category: null,
      },
      queryParamsHandling: 'merge',
      replaceUrl: true,
    });
    this.searchGames();
  }

  searchGames() {
    this.gameService.searchGames(this.category, this.name).subscribe(
      (response) => {
        this.games = response.content;
        this.moreGames = !response.last;
      },
      (_) => {
        this.games = [];
        this.moreGames = false;
      }
    );
  }

  searchMoreGames() {
    this.loading = true;
    this.gameService
      .moreFoundGames(this.category, this.name, this.indexGames)
      .subscribe((response) => {
        this.games = this.games.concat(response.content);
        this.moreGames = !response.last;
        this.indexGames++;
        this.loading = false;
      });
  }

  goToGame(id: number) {
    this.router.navigate(['/game/' + id]);
  }
}
