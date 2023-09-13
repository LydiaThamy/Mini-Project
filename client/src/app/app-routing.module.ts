import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './component/home/home.component';
import { CartComponent } from './component/cart/cart.component';
import { LoginComponent } from './component/login/login.component';
import { AuthoriseComponent } from './component/authorise/authorise.component';
import { CheckoutComponent } from './component/checkout/checkout.component';
import { ConfirmationComponent } from './component/confirmation/confirmation.component';
import { AuthGuardService } from './service/auth-guard.service';

const routes: Routes = [
  {path: '', component: HomeComponent, title: 'ShopHouse'},
  {path: 'cart', component: CartComponent, title: 'Cart'},
  {path: 'login', component: LoginComponent, title: 'Login'},
  {path: 'authorise', component: AuthoriseComponent, title: 'Authenticating'},
  {path: 'checkout', component: CheckoutComponent, canActivate: [AuthGuardService], title: 'Checkout'},
  {path: 'confirmation', component: ConfirmationComponent, title: 'Order Confirmed'},
  {path: '**', redirectTo:'/authorise', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
