import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClientService } from 'src/app/service/client.service';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {

  sub$!: Subscription
  categories!: string[]
  constructor(private service: ClientService, private fb: FormBuilder, private router: Router) { }
  
  ngOnInit(): void {
    this.getCategories()
  }
export class HomeComponent {
  
  getCategories(): void {
    this.sub$ = this.service.getCategories()
      .subscribe(data => {
        this.categories = data as string[]
      })
  }
  
  ngOnDestroy(): void {
    this.sub$.unsubscribe()
  }
}
