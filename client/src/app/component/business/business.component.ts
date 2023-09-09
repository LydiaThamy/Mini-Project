import { Component, OnInit, ViewChild, OnDestroy, Input, Output } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ClientService } from 'app/service/client.service';
import { Subject, Subscription } from "rxjs";
import { Business } from 'app/interface/Business';
import { } from 'googlemaps';
import { Service } from 'app/interface/Service';
import { Review } from 'app/interface/Review';

@Component({
  selector: 'app-business',
  templateUrl: './business.component.html',
  styleUrls: ['./business.component.css']
})
export class BusinessComponent implements OnInit, OnDestroy {

  biz!: Business
  @Input() businessId!: number
  @Output() returnResults = new Subject<boolean>()

  svcs: Service[] = []
  rvws: Review[] = []

  biz$!: Subscription
  svc$!: Subscription
  rvw$!: Subscription
  crt$!: Subscription

  add$!: Subscription
  @ViewChild('map') mapElement: any;
  address: any = {
    lat: 0,
    lng: 0
  }

  constructor(private service: ClientService, private router: Router) { }

  ngOnInit(): void {
    this.biz$ = this.getBusinessById()

  }

  getBusinessById(): Subscription {
    return this.service.getBusinessById(this.businessId)
      .subscribe({
        next: e => {
          this.biz = {
            businessId: e['businessId'],
            businessName: e['businessName'],
            address: e['address'],
            phone: e['phone'],
            email: e['email'],
            website: e['website'],
            logo: e['logo']
          }

          this.getServicesByBusinessId()
          this.getReviewsByBusinessId()
          this.getGeocodedAddress()
        },
        error: () => {
          alert("No such business ID")
          this.router.navigate(['/'])
        }
      })
  }

  getGeocodedAddress(): void {
    this.add$ = this.service.getGeocode(this.biz.address)
      .subscribe({
        next: data => {
            // this.address.lat = data.features[0].properties.lat as number,
            //   this.address.lng = data.features[0].properties.lon as number
            this.address.lat = data.results[0].geometry.location.lat as number,
              this.address.lng = data.results[0].geometry.location.lng as number

            this.initMap()
        }
      })
  }
  
  initMap(): void {

    let map = new google.maps.Map(
      document.getElementById('map') as HTMLElement,
      { zoom: 17, center: this.address } as google.maps.MapOptions
    )

    let marker = new google.maps.Marker({
      position: this.address,
      map: map
    })
  }

  getServicesByBusinessId(): void {
    this.svc$ = this.service.getServicesByBusinessId(this.businessId)
      .subscribe({
        next: (data) => {
          data.forEach((e: any) => {
            this.svcs.push({
              serviceId: e['serviceId'],
              title: e['title'],
              description: e['description'],
              price: e['price']
            })
          })
        }
      })
  }

  getReviewsByBusinessId(): void {
    this.rvw$ = this.service.getReviewsByBusinessId(this.businessId)
      .subscribe({
        next: (data) =>
          data.forEach((e: any) => {
            this.rvws.push({
              reviewId: e['reviewId'],
              reviewer: e['reviewer'],
              content: e['content'],
              rating: e['rating'],
              reviewDate: e['reviewDate']
            })
          })
      })
  }

  addToCart(serviceId: number) {
    // call server to add cart details to cart
    this.crt$ = this.service.addCart(serviceId)
    .subscribe({
      next: () => alert("Added to cart"),
      error: (e) => alert(JSON.stringify(e))
    })
  }

  ngOnDestroy(): void {
    if (this.biz$ !== undefined)
    this.biz$.unsubscribe()

    if (this.add$ !== undefined)
    this.add$.unsubscribe()
  
    if (this.svc$ !== undefined)
    this.svc$.unsubscribe()
  
    if (this.rvw$ !== undefined)
    this.rvw$.unsubscribe()
  
    if (this.crt$ !== undefined)
    this.crt$.unsubscribe()
  }

}
