import { Component, OnInit } from '@angular/core';
import { Search } from 'app/interface/Search';
import { ClientService } from 'app/service/client.service';
import {  Subscription } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  selectedCategory: string = ''
  searchTerms: Search = {}
  categories: string[] = []
  sub$!: Subscription

  inspectBusiness: boolean = false
  bizId!: number

  constructor(private service: ClientService) {}

  ngOnInit(): void {
    this.getCategories()
    }

  changeCategory(category: string) {
    this.inspectBusiness = false
    this.selectedCategory = category
  }

  searchKeyword(search: Search) {
    this.inspectBusiness = false
    this.searchTerms = search
  }

  getCategories(): void {
    this.sub$ = this.service.getCategories()
      .subscribe(data => {
        this.categories = data as string[]
      })
  }

  selectBusiness(id: number): void {
    this.bizId = id
    this.inspectBusiness = !this.inspectBusiness
  }
  
}
