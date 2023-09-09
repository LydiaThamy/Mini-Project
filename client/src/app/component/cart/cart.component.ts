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
  constructor(private service: ClientService) { }

  ngOnInit(): void {
    this.getCart()
  }

  getCart(): void {
    this.sub$ = this.service.getCart()
      .subscribe({
        next: data => {
          data.forEach((e: any) => {
            Object.keys(e).forEach((key: any) => {
              this.cart.push({
                serviceId: key,
                quantity: e[key]
              } as Item)
            });
          });
        },
        error: e => alert(JSON.stringify(e))
      })
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe()
  }
}
