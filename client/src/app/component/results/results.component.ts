import { Component, Input, OnChanges, OnDestroy, Output } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, Subscription } from "rxjs";
import { Business } from 'app/interface/Business';
import { Search } from 'app/interface/Search';
import { BusinessService } from 'app/service/business.service';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements OnChanges, OnDestroy {

  @Input() category!: string
  @Input() search!: Search
  @Output() selectedBizId = new Subject<number>()

  sub$!: Subscription
  businesses!: Business[]

  constructor(private service: BusinessService, private router: Router) { }

  ngOnChanges(): void {
    if (this.category.length > 0) {
      this.getBusinessesByCategory()
    } else if (this.search.keyword !== undefined) {
      this.getBusinessesByKeyword()
    } else {
      this.getAllBusinesses()
    }
  }

  getAllBusinesses(): void {
    this.businesses = []

    this.sub$ = this.service.getAllBusinesses()
      .subscribe(data => this.addBusiness(data))
  }

  getBusinessesByCategory(): void {
    this.businesses = []

    this.sub$ = this.service.getBusinessesByCategory(this.category)
      .subscribe({
        next: data => this.addBusiness(data),
        complete: () => { this.category = '' }
      })
  }

  getBusinessesByKeyword(): void {
    this.businesses = []

    this.sub$ = this.service.getBusinessesByKeyword(this.search)
      .subscribe({
        next: data => this.addBusiness(data),
        complete: () => { this.search = {} }
      })
  }

  addBusiness(data: any) {
    data.forEach((e: any) => {
      this.businesses.push({
        businessId: e['businessId'],
        businessName: e['businessName'],
        address: e['address'],
        phone: e['phone'],
        email: e['email'],
        website: e['website'],
        logo: e['logo']
      })
    })
  }

  searchBusiness(id: number | undefined): void {
    if (id !== undefined)
      this.selectedBizId.next(id)
    // this.router.navigate(['/business', id])
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe()
  }

}
