import { NgModule, isDevMode } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './component/home/home.component';
import { ClientService } from './service/client.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from "@angular/common/http";
import { ErrorComponent } from './component/error/error.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './ng-material/ng-material.module';
import { ResultsComponent } from './component/results/results.component';
import { HeaderComponent } from './component/header/header.component';
import { BusinessComponent } from './component/business/business.component';
import { ServiceWorkerModule } from '@angular/service-worker';
import { CartComponent } from './component/cart/cart.component';
import { LoginComponent } from './component/login/login.component';
import { LogoComponent } from './component/logo/logo.component';
import { CheckoutComponent } from './component/checkout/checkout.component';
import { ConfirmationComponent } from './component/confirmation/confirmation.component';
import { AuthoriseComponent } from './component/authorise/authorise.component';
import { CartService } from './service/cart.service';
import { UserService } from './service/user.service';
import { BusinessService } from './service/business.service';
import { SearchBarComponent } from './component/search-bar/search-bar.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ErrorComponent,
    ResultsComponent,
    HeaderComponent,
    BusinessComponent,
    CartComponent,
    LoginComponent,
    LogoComponent,
    CheckoutComponent,
    ConfirmationComponent,
    AuthoriseComponent,
    SearchBarComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MaterialModule,
    ServiceWorkerModule.register('ngsw-worker.js', {
      enabled: !isDevMode(),
      // Register the ServiceWorker as soon as the application is stable
      // or after 30 seconds (whichever comes first).
      registrationStrategy: 'registerWhenStable:30000'
    })
  ],
  providers: [
    ClientService,
    CartService,
    UserService,
    BusinessService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
