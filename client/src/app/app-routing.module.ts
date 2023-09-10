import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './component/home/home.component';
import { ErrorComponent } from './component/error/error.component';
// import { BusinessComponent } from './component/business/business.component';
import { LoginComponent } from './component/login/login.component';
import { CartComponent } from './component/cart/cart.component';
// import { CreateAccountComponent } from './component/create-account/create-account.component';
import { CheckoutComponent } from './component/checkout/checkout.component';
import { ConfirmationComponent } from './component/confirmation/confirmation.component';
import { authGuardFactory } from './auth.guard';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'cart', component: CartComponent},
  {path: 'login', component: LoginComponent},
  {path: 'checkout', component: CheckoutComponent, canActivate: [authGuardFactory]},
  {path: 'confirmation', component: ConfirmationComponent},
  // {path: 'create-account', component: CreateAccountComponent},
  {path: '**', component: ErrorComponent, pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
