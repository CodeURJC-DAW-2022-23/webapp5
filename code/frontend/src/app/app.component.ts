import { ScrollToTopService } from './ScrollTopService.component';
import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
})
export class AppComponent {
  title = 'KeyWhale';
  constructor(
    private titleService: Title,
    private scrollToTopService: ScrollToTopService
  ) {}
  ngOnInit() {
    this.titleService.setTitle(this.title);
    this.scrollToTopService.init();
  }
}
