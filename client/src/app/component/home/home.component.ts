import { Component, DoCheck, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClientService } from 'src/app/service/client.service';
import { Subscription, switchMap } from 'rxjs';
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
  autocomplete: string[] = []
  constructor(private service: ClientService, private fb: FormBuilder, private router: Router) { }


  ngOnInit(): void {
    this.getCategories()
    this.createForm()
    this.autocompleteKeyword()
  }

  autocompleteKeyword(): void {
    this.sub$ = this.searchForm.controls['keyword'].valueChanges
      .pipe(
        switchMap((value: string) => {
          return this.service.autocompleteKeyword(value.toLowerCase());
        })
      )
      .subscribe({
        next: (data) => {
          this.autocomplete = []

          if (data != null)
            data.forEach((e: string) => {

              if (!this.autocomplete.includes(e)) 
                this.autocomplete.push(e)
            });
        }
      })
  }

  createForm(): void {
    this.searchForm = this.fb.group({
      category: this.fb.control<string>('', [Validators.required]),
      region: this.fb.control<string>('', [Validators.required]),
      keyword: this.fb.control<string>('')
    })
  }

  getCategories(): void {
    this.sub$ = this.service.getCategories()
      .subscribe(data => {
        this.categories = data as string[]
      })
  }

  searchKeyword(): void {
    // const category: string = this.searchForm.value['category']
    // const region: string = this.searchForm.value['region']

    const keyword: string = this.searchForm.value['keyword']
    // this.sub$ = this.service.search(category, region)
    this.sub$ = this.service.searchKeyword(keyword)
      .subscribe({
        next: data => {
          this.router.navigate(['/results'],
            { queryParams: { keyword: keyword } })
            // { queryParams: { category: category, region: region } })
        },
        error: () => {
          alert('No results found based on your current selection')
        }
      })
  }

  searchCategory(category: string): void {
    this.router.navigate(['/results'], { queryParams: { category: category } })
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe()
  }
}
