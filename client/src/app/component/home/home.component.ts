import { Component } from '@angular/core';
import { Search } from 'app/interface/Search';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

  selectedCategory: string = ''
  searchTerms: Search = {}

  changeCategory(category: string) {
    this.selectedCategory = category
  }

  searchKeyword(search: Search) {
    this.searchTerms = search
  }
}
