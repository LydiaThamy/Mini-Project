import { Component, OnDestroy, OnInit } from '@angular/core';
import { Item } from 'app/interface/Item';
import { CartService } from 'app/service/cart.service';
import { ClientService } from 'app/service/client.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit, OnDestroy {

  sub$!: Subscription
  loadComplete: boolean = false

  cart: Item[] = []
  constructor(private service: CartService) { }

  ngOnInit(): void {
    this.getCart()
  }

  getCart(): void {
    this.sub$ = this.service.getCart()
      .subscribe({
        next: data => {
          data.forEach((e: any) => {
            this.cart.push({
              serviceId: e.serviceId,
              quantity: e.quantity,
              title: e.title,
              businessName: e.businessName
            } as Item)
          });
          this.loadComplete = true
        },
        error: (e) => {
          this.loadComplete = true
        }
      })
  }

  minusItem(id: number) {
    const idx: number = this.cart.findIndex(
      (i: Item) => i.serviceId == id)
    this.cart[idx].quantity--

    // delete if quantity is 0
    if (this.cart[idx].quantity == 0)
      this.deleteItem(id)
  }

  addItem(id: number) {
    const idx: number = this.cart.findIndex(
      (i: Item) => i.serviceId == id)
    this.cart[idx].quantity++
  }

  deleteItem(id: number) {
    const idx: number = this.cart.findIndex(
      (i: Item) => i.serviceId == id)
    this.cart.splice(idx, 1)
  }
  
  ngOnDestroy(): void {
    // update cart
    this.sub$ = this.service.updateCart(this.cart)
      .subscribe({
        complete: () => this.sub$.unsubscribe()
      })
  }
}
