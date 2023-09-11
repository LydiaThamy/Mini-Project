import { CartService } from 'app/service/cart.service';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { loadStripe } from '@stripe/stripe-js';
import { environment } from 'app/environment/environment';
import { User } from 'app/interface/User';
import { ClientService } from 'app/service/client.service';
import { Subscription } from 'rxjs';
import { Item } from 'app/interface/Item';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnDestroy {

  constructor(private service: ClientService, private cartSvc: CartService, private aRoute: ActivatedRoute) { }

  // cart: Item[] = []
  serviceId: string = this.aRoute.snapshot.queryParams['serviceId']
  item!: Item

  payment: any
  stripePromise = loadStripe(environment.stripe)
  sub$!: Subscription

  ngOnInit(): void {
    this.sub$ = this.cartSvc.getItem(this.serviceId)
      .subscribe({
        next: e => {
          console.log(JSON.stringify(e))
          this.item = {
            serviceId: e.serviceId,
            quantity: e.quantity,
            title: e.title,
            businessName: e.businessName,
            price: e.price,
            logo: e.logo
          }
        }
      })
  }

  // getCart(): void {
  //   this.sub$ = this.cartSvc.getCart()
  //     .subscribe({
  //       next: data => {
  //         data.forEach((e: any) => {
  //           this.cart.push({
  //             serviceId: e.serviceId,
  //             quantity: e.quantity,
  //             title: e.title,
  //             businessName: e.businessName,
  //             price: e.price,
  //             logo: e.logo
  //           } as Item)
  //         });
  //       }
  //     })
  // }

  // here we create a payment object
  createPayment() {

    if (this.item.price == undefined)
      this.item.price = 0

    this.payment = {
      name: this.item.title,
      currency: 'sgd',
      // amount on cents *10 => to be on dollar
      amount: this.item.price * 10,
      quantity: this.item.quantity,
      successUrl: 'http://localhost:4200/#/confirmation',
      cancelUrl: 'http://localhost:4200/#/checkout',
    }

    // let lineItems: any[] = []
    // for (let c of this.cart) {
    //   let data: any[] = []
    //   data.push(
    //     // { name: c.title },
    //     { descripton: c.title + ' by ' + c.businessName },
    //     { amount_total: c.price },
    //     { quantity: c.quantity }
    //   )
    //   lineItems.push({data: data})
    // }
    // console.log(JSON.stringify(lineItems))
  }

  async pay(): Promise<void> {
    // create payment object
    this.createPayment()

    const stripe = await this.stripePromise;

    this.service.makePayment(this.payment)
      .subscribe((data: any) => {
        while (stripe == null) { }
        // I use stripe to redirect To Checkout page of Stripe platform
        stripe.redirectToCheckout({
          sessionId: data.id,
        });
      });
  }

  ngOnDestroy(): void {
    this.service.checkoutItem = this.item
  }
}
