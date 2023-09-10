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

  constructor(private http: HttpClient) {
    this.customerId = this.getCustomerId()
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

  addCart(serviceId: number): Observable<any> {
    const item: any = {
      serviceId: serviceId
    }
    return this.http.post(`/api/shophouse/cart/add/${this.customerId}`, {serviceId: serviceId})
    // return this.http.post(`/api/shophouse/cart/add/${this.customerId}`, item)
  }

  updateCart(cart: Item[]) {
    return this.http.put(`/api/shophouse/cart/update/${this.customerId}`, cart)
  }

  getCart(): Observable<any> {
    return this.http.get(`/api/shophouse/cart/${this.customerId}`)
  }

  getCategories(): Observable<any> {
    return this.http.get("/api/shophouse/categories")
  }

  autocompleteKeyword(keyword: string): Observable<any> {
    const httpParams = new HttpParams()
      .set('keyword', keyword)
    return this.http.get('/api/shophouse/autocomplete', { params: httpParams })
  }

  getAllBusinesses(): Observable<any> {
    return this.http.get("/api/shophouse/businesses")
  }

  getBusinessesByKeyword(search: Search): Observable<any> {

    let httpParam: HttpParams = new HttpParams()

    if (search.keyword !== undefined)
      httpParam = httpParam.set('keyword', search.keyword.toString())

    if (search.category !== undefined)
      httpParam = httpParam.set('category', search.category.toString())

    if (search.region !== undefined)
      httpParam = httpParam.set('region', search.region.toString())

    return this.http.get('/api/shophouse/businesses/keyword', { params: httpParam })
  }

  getBusinessesByCategory(category: string): Observable<any> {
    let httpParam: HttpParams = new HttpParams()
      .set('category', category)
    return this.http.get('/api/shophouse/businesses/category', { params: httpParam })
  }

  getBusinessById(id: number): Observable<any> {
    return this.http.get(`/api/shophouse/business/${id}`)
  }

  getServicesByBusinessId(id: number): Observable<any> {
    return this.http.get(`/api/shophouse/business/${id}/services`)
  }

  getReviewsByBusinessId(id: number): Observable<any> {
    return this.http.get(`/api/shophouse/business/${id}/reviews`)
  }

  getGeocode(address: string): Observable<any> {
    const httpParams = new HttpParams()
      .set("address", address)
    return this.http.get("/api/shophouse/geocode", { params: httpParams })
  }

  getUser(): Observable<any> {
    return this.http.get("/api/shophouse/user")
  }

  makePayment(payment: any): Observable<any> {
    return this.http.post("/api/shophouse/payment", payment)
  }

}
