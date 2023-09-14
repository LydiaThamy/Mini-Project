import { Component } from '@angular/core';
import { environment } from 'app/environment/environment';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  githubUrl: string = `${environment.baseUrl}/oauth2/authorization/github`
  
  // private userService = inject(UserService);
  // private router = inject(Router);
  // githubUrl: string = `${environment.baseUrl}/oauth2/authorization/github`

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