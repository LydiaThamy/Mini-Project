import { Component, OnDestroy, OnInit, Output } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { ClientService } from 'app/service/client.service';
import { Subject, Subscription, switchMap } from 'rxjs';
import { Router } from '@angular/router';
import { Search } from 'app/interface/Search';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {

  categories: string[] = []
  regions: string[] = ['central', 'east', 'north', 'northeast', 'west']

  searchForm!: FormGroup
  openFilter: boolean = false

  @Output() search = new Subject<Search>()
  // @Output() searchedKeyword = new Subject<string>()
  // @Output() searchedCategory = new Subject<string>()
  // @Output() searchedRegion = new Subject<string>()
  @Output() selectedCategory = new Subject<string>()
  // selectedCategory: string = 'popular'

  sub$!: Subscription
  autocomplete: string[] = []

  constructor(private service: ClientService, private fb: FormBuilder, private router: Router) { }

  ngOnInit(): void {
    this.getCategories()
    this.createForm()
    this.autocompleteKeyword()
  }

  getCategories(): void {
    this.sub$ = this.service.getCategories()
      .subscribe(data => {
        this.categories = data as string[]
      })
  }

  createForm(): void {
    this.searchForm = this.fb.group({
      keyword: this.fb.control<string>('', [Validators.required]),
      category: new FormArray<FormControl<string>>([]),
      region: new FormArray<FormControl<string>>([])
    })
  }

  changeFilter(): void {
    this.openFilter = !this.openFilter
  }

  checkCategory(event: any): void {
    const formArr: FormArray = this.searchForm.get('category') as FormArray

    if (event.target.checked) {
      formArr.push(new FormControl(event.target.value))
    } else {
      let i: number = 0
      formArr.controls.forEach((ctr: AbstractControl<any, any>) => {
        if (ctr.value == event.target.value) {
          formArr.removeAt(i)
          return
        }
        i++
      })
    }
  }

  checkRegion(event: any): void {
    this.checkBox(event, 'region')
  }

  checkBox(event: any, formControlName: string) {
    const formArr: FormArray = this.searchForm.get(formControlName) as FormArray

    if (event.target.checked) {
      formArr.push(new FormControl(event.target.value))
    } else {
      let i: number = 0
      formArr.controls.forEach((ctr: AbstractControl<any, any>) => {
        if (ctr.value == event.target.value) {
          formArr.removeAt(i)
          return
        }
        i++
      })
    }
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

          if (data != null && data.length > 0)
            data.forEach((e: string) => {

              if (!this.autocomplete.includes(e))
                this.autocomplete.push(e)
            });
        }
      })
  }

  searchKeyword(formData: any, formDirective: FormGroupDirective): void {

    const keyword: string = this.searchForm.value['keyword']
    if (keyword.length < 1) {
      alert('Type a keyword to search for businesses')
      return
    }

    const category: string[] = this.searchForm.value['category']
    const region: string[] = this.searchForm.value['region']

    const searchTerms: Search = { keyword, category, region }

    this.sub$ = this.service.searchBusinesses(searchTerms)
      .subscribe({
        next: () => {
          this.search.next(searchTerms)
        },
        error: () => {
          alert('No results found based on your current selection')
        },
        complete: () => {
          formDirective.resetForm()
          this.searchForm.reset()
        }
      })
  }

  searchCategory(category: string): void {
    this.selectedCategory.next(category)
    // this.router.navigate(['/results'], { queryParams: { category: category } })
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe()
  }
}
