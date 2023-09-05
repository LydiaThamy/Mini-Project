import { ClientService } from 'src/app/service/client.service';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from "rxjs";
import { Business } from 'src/app/interface/Business';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements OnInit, OnDestroy {

  category!: string
  // region?: string
  keyword!: string
  sub$!: Subscription
  businesses: Business[] = []
  constructor(private route: ActivatedRoute, private service: ClientService) {}

  ngOnInit(): void {
    // this.region = this.route.snapshot.queryParams['region']
    this.category = this.route.snapshot.queryParams['category']
    
    if (this.category !== undefined) {
      this.getBusinessesByCategory()
    } else {
      this.getBusinesses()
    }
  }

  getBusinessesByCategory(): void {
    this.sub$ = this.service.searchCategory(this.category)
      .subscribe(data => {
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
        });
      })
  }


  searchBusiness(business: Business): void {

  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe()
  }


}
