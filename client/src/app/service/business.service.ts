import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Search } from 'app/interface/Search';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BusinessService {

  constructor(private http: HttpClient) { }

  getCategories(): Observable<any> {
    return this.http.get("/api/business/categories")
  }

  autocompleteKeyword(keyword: string): Observable<any> {
    const httpParams = new HttpParams()
      .set('keyword', keyword)
    return this.http.get('/api/business/autocomplete', { params: httpParams })
  }

  getAllBusinesses(): Observable<any> {
    return this.http.get("/api/business/")
  }

  getBusinessesByKeyword(search: Search): Observable<any> {

    let httpParam: HttpParams = new HttpParams()

    if (search.keyword !== undefined)
      httpParam = httpParam.set('keyword', search.keyword.toString())

    if (search.category !== undefined)
      httpParam = httpParam.set('category', search.category.toString())

    if (search.region !== undefined)
      httpParam = httpParam.set('region', search.region.toString())

    return this.http.get('/api/business/keyword', { params: httpParam })
  }

  getBusinessesByCategory(category: string): Observable<any> {
    let httpParam: HttpParams = new HttpParams()
      .set('category', category)
    return this.http.get('/api/business/category', { params: httpParam })
  }

  getBusinessById(id: number): Observable<any> {
    return this.http.get(`/api/business/${id}`)
  }

  getServicesByBusinessId(id: number): Observable<any> {
    return this.http.get(`/api/business/${id}/services`)
  }

  getReviewsByBusinessId(id: number): Observable<any> {
    return this.http.get(`/api/business/${id}/reviews`)
  }

  getBusinessByServiceId(serviceid: number): Observable<any> {
  return this.http.get(`/api/business/service/${serviceid}`)
  }
}
