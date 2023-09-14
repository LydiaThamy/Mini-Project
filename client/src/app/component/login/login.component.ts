import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  githubUrl: string = `http://localhost:8080/oauth2/authorization/github` // TODO change back
}