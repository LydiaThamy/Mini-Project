import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Item } from 'app/interface/Item';
import { Observable } from 'rxjs';
import { ulid } from 'ulid';
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
    return this.http.post(`/api/cart/add/${this.customerId}`, {serviceId: serviceId})
    // return this.http.post(`/api/shophouse/cart/add/${this.customerId}`, item)
  }

  updateCart(cart: Item[]) {
    return this.http.put(`/api/cart/update/${this.customerId}`, cart)
  }

  getCart(): Observable<any> {
    return this.http.get(`/api/cart/${this.customerId}`)
  }
}
