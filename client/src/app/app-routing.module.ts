import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ErrorComponent } from './component/error/error.component';
import { HomeComponent } from './component/home/home.component';
// import { BusinessComponent } from './component/business/business.component';
import { CartComponent } from './component/cart/cart.component';
import { LoginComponent } from './component/login/login.component';
// import { CreateAccountComponent } from './component/create-account/create-account.component';
import { authGuard } from './auth.guard';
import { AuthoriseComponent } from './component/authorise/authorise.component';
import { CheckoutComponent } from './component/checkout/checkout.component';
import { ConfirmationComponent } from './component/confirmation/confirmation.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'cart', component: CartComponent},
  {path: 'login', component: LoginComponent},
  {path: 'authorise', component: AuthoriseComponent},
  {path: 'checkout', component: CheckoutComponent, canActivate: [authGuard]},
  {path: 'confirmation', component: ConfirmationComponent},
  // {path: 'create-account', component: CreateAccountComponent},
  {path: '**', component: ErrorComponent, pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
