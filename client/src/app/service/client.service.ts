import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor(private http: HttpClient) { }

  getCategories(): Observable<any> {
    return this.http.get("/api/shophouse/categories")
  }

  searchKeyword(keyword: string): Observable<any> {

    let httpParam: HttpParams = new HttpParams()
        .set('keyword', keyword)

    return this.http.get('/api/shophouse/businesses', { params: httpParam })
  }

  searchCategory(category: string): Observable<any> {
    let httpParam: HttpParams = new HttpParams()
        .set('c', category)
    return this.http.get('/api/shophouse/category', { params: httpParam })
  }

  // search(category: string, region?: string): Observable<any> {

  //   let regionParam: HttpParams

  //   if (region !== undefined) {
  //     regionParam = new HttpParams()
  //       .set('category', category)
  //       .set('region', region)

  //   } else {
  //     regionParam = new HttpParams()
  //       .set('category', category)
  //   }

  //   return this.http.get('/api/shophouse/businesses', { params: regionParam })
  // }

  autocompleteKeyword(keyword: string): Observable<any> {
    const httpParams = new HttpParams()
      .set('keyword', keyword)
    return this.http.get('/api/shophouse/', {params: httpParams})
  }
}
