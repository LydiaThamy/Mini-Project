import { CartService } from 'app/service/cart.service';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { loadStripe } from '@stripe/stripe-js';
import { environment } from 'app/environment/environment';
import { User } from 'app/interface/User';
import { ClientService } from 'app/service/client.service';
import { Subscription } from 'rxjs';
import { Item } from 'app/interface/Item';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {

  stripePromise = loadStripe(environment.stripe)
  payment: any
  cart: Item[] = []
  sub$!: Subscription

  constructor(private service: ClientService, private cartSvc: CartService, private http: HttpClient) {}

  ngOnInit(): void {
    this.getCart()
  }

  getCart(): void {
    this.sub$ = this.cartSvc.getCart()
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
        }
      })
  }

  // here we create a payment object
  createPayment() {
    this.payment = {
      name: 'Iphone',
      currency: 'sgd',
      // amount on cents *10 => to be on dollar
      amount: 99900,
      quantity: '1',
      // cancelUrl: '/cart',
      cancelUrl: 'http://localhost:4200/#/checkout',
      // successUrl: '/confirmation',
      successUrl: 'http://localhost:4200/#/confirmation',
    }
  }

  async pay(): Promise<void> {
    // create payment object
    this.createPayment()

    const stripe = await this.stripePromise;

    this.service.makePayment(this.payment)
      .subscribe((data: any) => {
        while (stripe == null) {}
        // I use stripe to redirect To Checkout page of Stripe platform
        stripe.redirectToCheckout({
          sessionId: data.id,
        });
      });
  }
}
