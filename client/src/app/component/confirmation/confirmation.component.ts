import { Component } from '@angular/core';
import { User } from 'app/interface/User';
import { ClientService } from 'app/service/client.service';

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.css']
})
export class ConfirmationComponent {

  user!: User

  constructor(private service: ClientService) {}

  ngOnInit(): void {
    this.getUser()
  }
  
  getUser(): void {
    this.service.getUser()
    .subscribe({
      next: data => {
        this.user = {
          userId: data.userId,
          username: data.username,
          email: data.email
        }
      }
    })
  }
}
