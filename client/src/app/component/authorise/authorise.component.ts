import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'app/service/user.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-authorise',
  templateUrl: './authorise.component.html',
  styleUrls: ['./authorise.component.css']
})
export class AuthoriseComponent implements OnInit {

  constructor(private service: UserService, private router: Router) { }

  ngOnInit(): void {
    const sub$: Subscription = this.service.authenticateUser()
      .subscribe({
        next: data => {
          console.log("authorise user...")
          console.log(JSON.stringify(data))

          if (data.token && data.userId) {
            localStorage.setItem("userId", data.userId as string)
            localStorage.setItem("token", data.token as string)
          }

          console.log("navigating to checkout...")
          this.router.navigate(['/checkout'])
        },
        error: (e) => {
          console.log(JSON.stringify(e))
          this.router.navigate(['/login'])
        },
        complete: () => sub$.unsubscribe()
      })
  }
}
