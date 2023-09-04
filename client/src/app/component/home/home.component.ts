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
  searchForm!: FormGroup
  categories!: string[]
  constructor(private service: ClientService, private fb: FormBuilder, private router: Router) { }
  
  ngOnInit(): void {
    this.createForm()
    this.getCategories()
  }
  
  createForm(): void {
    this.searchForm = this.fb.group({
      category: this.fb.control<string>('', [Validators.required]),
      region: this.fb.control<string>('', [Validators.required])
    })
  }
  
  getCategories(): void {
    this.sub$ = this.service.getCategories()
      .subscribe(data => {
        this.categories = data as string[]
      })
  }

  search(): void {
    const category: string = this.searchForm.value['category']
    const region: string  = this.searchForm.value['region']
    this.router.navigate(['/results', category], {queryParams: {region: region}})
  }

  searchCategory(category: string): void {
    this.router.navigate(['/results', category])
  }
  
  ngOnDestroy(): void {
    this.sub$.unsubscribe()
  }
}
