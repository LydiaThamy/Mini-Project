import { Component, OnInit } from '@angular/core';
import { Search } from 'app/interface/Search';
import { ClientService } from 'app/service/client.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

  selectedCategory: string = ''
  searchTerms: Search = {}

  constructor() {}

  changeCategory(category: string) {
    this.selectedCategory = category
  }

  searchKeyword(search: Search) {
    this.searchTerms = search
  }
}
