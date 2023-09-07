import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './component/home/home.component';
import { ErrorComponent } from './component/error/error.component';
import { BusinessComponent } from './component/business/business.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'business/:id', component: BusinessComponent},
  {path: '**', component: ErrorComponent, pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
