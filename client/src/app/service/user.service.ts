import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ClientService } from './client.service';
import { User } from 'app/interface/User';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient, private service: ClientService) { }

  authenticateUser(): Observable<any> {
    return this.http.get(`/api/user/authorise`)
  }

  getUser(): Observable<any> {
    let token = this.service.getToken()
    console.log(token)
    return this.http.get(`/api/user/${token}`)
  }

  login(user: User): Observable<User> {
    const authData = btoa(`${user.email}:${user.password}`);
    const headers = new HttpHeaders().set('Authorization', `Basic ${authData}`);
    return this.http.post<User>(`/api/user/login`, null, { headers });
  }
}
