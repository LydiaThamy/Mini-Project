import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'app/interface/User';
import { UserService } from 'app/service/user.service';
import { first } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup
  githubUrl: string = `/oauth2/authorization/github`
  
  constructor(private router: Router, private fb: FormBuilder, private userSvc: UserService) {}
  
  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: this.fb.control<string>('', [Validators.required]),
      password: this.fb.control<string>('', [Validators.required])
    })
  }

  navigate(): void {
    this.router.navigate([this.githubUrl])
  }

  login() {
    const user: User = {
      username: this.loginForm.controls['username'].value,
      password: this.loginForm.controls['password'].value,
    }

    console.log(JSON.stringify(user))
    this.userSvc.login(user)
      .pipe(first())
      .subscribe({
        next: (data: any) => {
          localStorage.setItem('token', data.token)
           this.router.navigate(['/checkout'])
        },
        error: () => { alert('Invalid email or password')},
      });
    }

}
