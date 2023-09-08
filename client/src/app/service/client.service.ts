import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core'
import { Observable } from 'rxjs';
import { Search } from '../interface/Search';
import { ulid } from 'ulid'

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  customerId: string = this.getCustomerId()

  constructor(private http: HttpClient) {}

  getCustomerId(): string {
    let retrievedId: string | null = localStorage.getItem('customerId')
    let cId!: string
    
    // if localstorage has no customer ID or timestamp is more than a week
    if (retrievedId == null || new Date().getTime() - JSON.parse(retrievedId).timestamp as number <= 7 * 24 * 60 * 60 * 1000) {
      // make new customer ID
      cId = ulid()

    } else {
      cId = JSON.parse(retrievedId).cId
    }

    const customerId = {
      cId: cId,
      timestamp: new Date().getTime()
    }

    // set in localstorage
    localStorage.setItem('customerId', JSON.stringify(customerId))

    return cId
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

  searchBusinesses(search: Search): Observable<any> {

    let httpParam: HttpParams = new HttpParams()

    if (search.keyword !== undefined)
      httpParam = httpParam.set('keyword', search.keyword.toString())

    if (search.category !== undefined)
      httpParam = httpParam.set('category', search.category.toString())

    if (search.region !== undefined)
      httpParam = httpParam.set('region', search.region.toString())

    return this.http.get('/api/shophouse/businesses', { params: httpParam })
  }

  searchCategory(category: string): Observable<any> {
    return this.http.get(`/api/shophouse/category/${category}`)
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

}
