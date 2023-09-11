import { Item } from 'app/interface/Item';
import { CartService } from 'app/service/cart.service';
import { ClientService } from 'app/service/client.service';
import { Component, OnDestroy } from '@angular/core';
import { User } from 'app/interface/User';
import { UserService } from 'app/service/user.service';
import { BusinessService } from 'app/service/business.service';
import { Subscription } from 'rxjs';
import { Business } from 'app/interface/Business';
import { Router } from '@angular/router';

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.css']
})
export class ConfirmationComponent implements OnDestroy {

  user!: User
  sub$!: Subscription
  serviceId!: string
  business!: Business

  constructor(private userSvc: UserService, private router: Router, private bizSvc: BusinessService, private cartSvc: CartService) { }

  ngOnInit(): void {
    this.serviceId = sessionStorage.getItem("serviceId") as string
    
    if (!this.serviceId) {
      this.router.navigate(['/'])
      console.log(this.serviceId)

    } else {
      this.getUser()
    }
  }

  getUser(): void {
    this.sub$ = this.userSvc.getUser()
      .subscribe({
        next: data => {
          this.user = {
            userId: data.userId,
            username: data.username,
            email: data.email
          }

          this.deleteItem()
          this.getBusiness()
        },
        error: () => localStorage.removeItem("token")
      })
  }
  // remove item from cart
  deleteItem() {
    this.cartSvc.deleteItem(this.serviceId)
  }

  getBusiness(): void {
    this.sub$ = this.bizSvc.getBusinessByServiceId(this.serviceId)
      .subscribe({
        next: data => {
          this.business = {
            businessName: data.businessName,
            email: data.email
          }
        }
      })
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe()
  }
}
