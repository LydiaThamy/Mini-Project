import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  githubUrl: string = `/oauth2/authorization/github`
  constructor(private router: Router) {}

  navigate(): void {
    this.router.navigate([this.githubUrl])
  }
  // githubUrl: string = `/oauth2/authorization/github`
  
  // private userService = inject(UserService);
  // private router = inject(Router);
  // githubUrl: string = `/oauth2/authorization/github`

  // onClick() {
  //   this.userService.authenticateUser()
  //   .pipe(first())
  //   .subscribe({
  //     next: data => { 
  //       console.log(data)
  //       return this.router.navigate(['/checkout'])
  //      },
  //     error: err => {
  //       console.error(err)
  //      }
  //   })
  // }
}