import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Item } from 'app/interface/Item';
import { Observable, Subscription } from 'rxjs';
import { ClientService } from './client.service';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  customerId: string

  constructor(private http: HttpClient, private service: ClientService) {
    this.customerId = this.service.getCustomerId()
  }

  addCart(serviceId: number): Observable<any> {
    const item: any = {
      serviceId: serviceId
    }
    return this.http.post(`/api/cart/add/${this.customerId}`, { serviceId: serviceId })
    // return this.http.post(`/api/shophouse/cart/add/${this.customerId}`, item)
  }

  updateCart(cart: Item[]) {
    return this.http.put(`/api/cart/update/${this.customerId}`, cart)
  }

  getCart(): Observable<any> {
    return this.http.get(`/api/cart/${this.customerId}`)
  }

  deleteItem(serviceId: string): void {
    const httpParams: HttpParams = new HttpParams()
      .set("customerId", this.service.customerId)

    const sub$: Subscription = this.http.delete(`/api/cart/delete/${serviceId}`, {params: httpParams})
    .subscribe({
      complete: () => sub$.unsubscribe()
    })
  }

  getItem(serviceId: string): Observable<any> {
    const httpParams: HttpParams = new HttpParams()
      .set("customerId", this.service.customerId)

    return this.http.get(`/api/cart/item/${serviceId}`, {params: httpParams})
  }
}
