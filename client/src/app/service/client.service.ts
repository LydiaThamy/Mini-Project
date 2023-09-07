import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Search } from '../interface/Search';
import { environment } from '../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor(private http: HttpClient) { }

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

    return this.http.get('/api/shophouse/businesses', {params: httpParam})
  }

  searchCategory(category: string): Observable<any> {
    return this.http.get(`/api/shophouse/category/${category}`)
  }

  getBusinessById(id: number): Observable<any> {
    return this.http.get(`/api/shophouse/business/${id}`)
  }

  getGeocode(address: string): Observable<any> {
    const httpParams = new HttpParams()
      .set("address", address)
    return this.http.get("/api/shophouse/geocode", {params: httpParams})
  }
}
