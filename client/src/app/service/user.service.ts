import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ClientService } from './client.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient, private service: ClientService) { }

  authenticateUser(): Observable<any> {
    return this.http.get("/api/user/authorise")
  }

  getUser(): Observable<any> {
    let token = this.service.getToken()
    console.log(token)
    return this.http.get(`/api/user/${token}`)
  }
}
