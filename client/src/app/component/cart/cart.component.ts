import { Component, OnDestroy, OnInit } from '@angular/core';
import { Item } from 'app/interface/Item';
import { ClientService } from 'app/service/client.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit, OnDestroy {

  cart: Item[] = []
  sub$!: Subscription
  constructor(private service: ClientService){}

  ngOnInit(): void {
    this.sub$ = this.service.getCart()
      .subscribe((data) => 
          this.cart.push({
            serviceId: data.serviceId as number,
            quantity: data.quantity as number
          } as Item)
      )
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe()
  }
}
