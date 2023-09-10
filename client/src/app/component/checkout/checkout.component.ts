import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { loadStripe } from '@stripe/stripe-js';
import { environment } from 'app/environment/environment';
import { User } from 'app/interface/User';
import { ClientService } from 'app/service/client.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent {


  stripePromise = loadStripe(environment.stripe)
  payment: any

  constructor(private service: ClientService, private http: HttpClient) {}

  ngOnInit(): void {
    this.getToken()
  }

  getToken(): void {
    this.service.authenticateUser()
      .subscribe({
        next: data => localStorage.setItem("token", data as string)
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
        if (stripe != null)
        // I use stripe to redirect To Checkout page of Stripe platform
        stripe.redirectToCheckout({
          sessionId: data.id,
        });
      });
  }
}
