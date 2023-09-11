import { Item } from 'app/interface/Item';
import { CartService } from 'app/service/cart.service';
import { ClientService } from 'app/service/client.service';
import { Component, OnDestroy } from '@angular/core';
import { User } from 'app/interface/User';
import { UserService } from 'app/service/user.service';
import { BusinessService } from 'app/service/business.service';
import { Subscription } from 'rxjs';
import { Business } from 'app/interface/Business';

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.css']
})
export class ConfirmationComponent implements OnDestroy {

  user!: User
  sub$!: Subscription
  item!: Item
  business!: Business

  constructor(private userSvc: UserService, private clientSvc: ClientService, private bizSvc: BusinessService, private cartSvc: CartService) {}

  ngOnInit(): void {
    this.getUser()
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
      }
    })
  }
    // remove item from cart
    deleteItem() {
      this.item = this.clientSvc.checkoutItem
      this.cartSvc.deleteItem(this.item.serviceId)
    }
  
    getBusiness(): void {
      this.sub$ = this.bizSvc.getBusinessByServiceId(this.item.serviceId)
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
