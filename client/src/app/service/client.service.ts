import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core'
import { Observable } from 'rxjs';
import { Search } from '../interface/Search';
import { ulid } from 'ulid'
import { Item } from 'app/interface/Item';
import { User } from 'app/interface/User';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  customerId: string
  user!: User
  checkoutItem!: Item

  constructor(private http: HttpClient) {
    this.customerId = this.getCustomerId()
  }

  getToken(): string | null {
    return localStorage.getItem("token")
  }

  getCustomerId(): string {
    let retrievedId: string | null = localStorage.getItem('customerId')
    let cId!: string

    // if localstorage has customer ID or timestamp is less than a week
    if (retrievedId != null && new Date().getTime() - JSON.parse(retrievedId).timestamp as number <= 7 * 24 * 60 * 60 * 1000) {
      cId = JSON.parse(retrievedId).cId
      
      // make new customer ID
    } else {
      cId = ulid()
    }

    const customerId = {
      cId: cId,
      timestamp: new Date().getTime()
    }

    // set in localstorage
    localStorage.setItem('customerId', JSON.stringify(customerId))
    return cId
  }

  getGeocode(address: string): Observable<any> {
    const httpParams = new HttpParams()
      .set("address", address)
    return this.http.get("/api/shophouse/geocode", { params: httpParams })
  }
  
  makePayment(payment: any): Observable<any> {
    return this.http.post("/api/payment/", payment)
  }

}
