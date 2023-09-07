import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { environment } from 'src/app/environment/environment'
import { ActivatedRoute } from '@angular/router';
import { ClientService } from 'src/app/service/client.service';
import { Subscription } from "rxjs";
import { Business } from 'src/app/interface/Business';
import { } from 'googlemaps';

@Component({
  selector: 'app-business',
  templateUrl: './business.component.html',
  styleUrls: ['./business.component.css']
})
export class BusinessComponent implements OnInit, OnDestroy {

  id: number = this.route.snapshot.params['id']
  @ViewChild('map') mapElement: any;

  sub$!: Subscription
  business!: Business
  address: any = {
    lat: 0,
    lng: 0
  }
  data!: any

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
    this.sub$ = this.service.getBusinessById(this.id)
      .subscribe({
        next: e => {
            this.business = {
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
          let add$: Subscription = this.service.getGeocode(this.business.address)
            .subscribe({
              next: data => {
                this.data = JSON.stringify(data)
                
                this.address.lat = data.results[0].geometry.location.lat as number,
                this.address.lng = data.results[0].geometry.location.lng as number

                this.initMap()
              },
              error: (e) => alert(e),
              complete: () => add$.unsubscribe()
            })
          }
        })
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe()
  }
}
