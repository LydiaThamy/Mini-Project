import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ClientService } from 'app/service/client.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  githubUrl: string = '/oauth2/authorization/github'
  // loginForm!: FormGroup
  // constructor(private fb: FormBuilder, private service: ClientService, private http: HttpClient, private router: Router) { }

  // ngOnInit(): void {
  //   // this.createForm()
  // }

  // createForm(): void {
  //   this.loginForm = this.fb.group({
  //     username: this.fb.control<string>('', [Validators.required]),
  //     password: this.fb.control<string>('', [Validators.required]),
  //   })
  // }

  // login(): void {
  //   const clientId = 'c8885ae2376e95046357';
  //   const redirectUri = encodeURIComponent('');
  //   const authEndpoint = `https://github.com/login/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUri}`;
    
  //   window.location.href = authEndpoint;
  // }

  // onSignIn(googleUser: any) {
  //   var profile = googleUser.getBasicProfile();
  //   console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
  //   console.log('Name: ' + profile.getName());
  //   console.log('Image URL: ' + profile.getImageUrl());
  //   console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
    
  // }

  // ngOnDestroy(): void {
  //   this.service.getUser()
  //   .subscribe({
  //     next: data => {
  //       this.service.user = {
  //         userId: data.userId,
  //         username: data.username,
  //         email: data.email
  //       }
        
  //       sessionStorage.setItem("user", JSON.stringify(this.service.user))
  //     }
  //   })
  // }
}