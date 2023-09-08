import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ClientService } from 'app/service/client.service';
import { Subscription } from "rxjs";
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
  businessId: number = this.route.snapshot.params['id']

  svcs: Service[] = []
  rvws: Review[] = []

  biz$!: Subscription
  svc$!: Subscription
  rvw$!: Subscription

  add$!: Subscription
  @ViewChild('map') mapElement: any;
  address: any = {
    lat: 0,
    lng: 0
  }

  cart: number[] = []

  constructor(private route: ActivatedRoute, private service: ClientService) { }

  ngOnInit(): void {
    this.getBusinessById()

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

  getBusinessById(): void {
    this.biz$ = this.service.getBusinessById(this.businessId)
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
        },
        complete: () => {
          this.getServicesByBusinessId()
          this.getReviewsByBusinessId()
          this.getGeocodedAddress()
        }
      })
  }

  getGeocodedAddress(): void {
    this.add$ = this.service.getGeocode(this.biz.address)
      .subscribe({
        next: data => {
          console.log(JSON.stringify(data))
            // this.address.lat = data.features[0].properties.lat as number,
            //   this.address.lng = data.features[0].properties.lon as number
            this.address.lat = data.results[0].geometry.location.lat as number,
              this.address.lng = data.results[0].geometry.location.lng as number

            this.initMap()
        }
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
    this.cart.push(serviceId)
    // call server to add cart details to cart
  }

  ngOnDestroy(): void {
    this.biz$.unsubscribe()
    this.add$.unsubscribe()
    this.svc$.unsubscribe()
    this.rvw$.unsubscribe()
  }
}
