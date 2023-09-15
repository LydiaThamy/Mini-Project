import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Search } from 'app/interface/Search';
import { BusinessService } from 'app/service/business.service';
import {  Subscription } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  selectedCategory: string = ''
 
  categories: string[] = []
  sub$!: Subscription
  
  inspectBusiness: boolean = false
  bizId!: number

  searchTerms: Search = {}

  over!: boolean[];
  overReturn = false

  constructor(private service: BusinessService, private aRoute: ActivatedRoute) {}

  ngOnInit(): void {
    if (this.aRoute.snapshot.queryParams['keyword']) {
      console.log(this.aRoute.snapshot.queryParams)
      this.searchTerms = {
        keyword: this.aRoute.snapshot.queryParams['keyword'],
        category: this.aRoute.snapshot.queryParams['category'],
        region: this.aRoute.snapshot.queryParams['region']
      }
    }

    this.getCategories()
    }

  changeCategory(category: string) {
    this.inspectBusiness = false
    this.selectedCategory = category
  }

  getCategories(): void {
    this.sub$ = this.service.getCategories()
      .subscribe(data => {
        this.categories = data as string[]
        this.over = new Array(this.categories.length);
        this.over.fill(false);
      })
  }

  selectBusiness(id: number): void {
    this.bizId = id
    this.inspectBusiness = !this.inspectBusiness
  }

  searchKeyword(search: Search) {
    this.inspectBusiness = false
    this.searchTerms = search
  }
}
