import { ClientService } from 'app/service/client.service';
import { Component, Input, OnChanges, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from "rxjs";
import { Business } from 'app/interface/Business';
import { Search } from 'app/interface/Search';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements OnChanges, OnDestroy {

  @Input() category!: string
  @Input() search!: Search
  // region?: string

  sub$!: Subscription
  businesses!: Business[]

  constructor(private service: ClientService, private router: Router) { }

  ngOnChanges(): void {
    if (this.category.length >= 1) {
      this.getBusinessesByCategory()
    }

    if (this.search.keyword !== undefined) {
      this.getBusinesses()
    }
  }

  getBusinessesByCategory(): void {
    this.businesses = []

    this.sub$ = this.service.searchCategory(this.category)
      .subscribe({
        next: data => {
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
      },
    complete: () => {this.category = ''}
    })
  }

  getBusinesses(): void {
    this.businesses = []

    this.sub$ = this.service.searchBusinesses(this.search)
      .subscribe({
        next: data => {
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
        },
        complete: () => {this.search = {}}
      })
  }

  searchBusiness(id: number): void {
    this.router.navigate(['/business', id])
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe()
  }

}
