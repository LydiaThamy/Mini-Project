import { ClientService } from 'app/service/client.service';
import { Component, OnDestroy, OnInit, Output } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { Subject, Subscription, switchMap } from 'rxjs';
import { Router } from '@angular/router';
import { Search } from 'app/interface/Search';
import { BusinessService } from 'app/service/business.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {

  constructor(private service: ClientService, private bizSvc: BusinessService, private fb: FormBuilder, private router: Router) { }
  
  customerId: string = this.service.customerId
  regions: string[] = ['central', 'east', 'north', 'northeast', 'west']

  searchForm!: FormGroup
  categoryForm: FormArray = new FormArray<FormControl<string>>([])
  regionForm: FormArray = new FormArray<FormControl<string>>([])

  openFilter: boolean = false
  categoryFilter: string[] = []
  regionFilter: string[] = []

  @Output() search = new Subject<Search>()

  sub$!: Subscription
  autocomplete: string[] = []
  categories: string[] = []

  ngOnInit(): void {
    this.getCategories()
    this.createForm()
    this.autocompleteKeyword()
  }

  createForm(): void {
    this.searchForm = this.fb.group({
      keyword: this.fb.control<string>('', [Validators.required]),
      category: this.categoryForm,
      region: this.regionForm
    })
  }

  getCategories(): void {
    this.sub$ = this.bizSvc.getCategories()
      .subscribe(data => {
        this.categories = data as string[]
      })
  }

  changeFilter(): void {

    if (this.openFilter == false) {
      if (this.categoryFilter.length > 0) {
        for (let c of this.categoryFilter) {
          if (this.categoryForm.get(c) != null)
            this.categoryForm.push(new FormControl(c))
        }
      }

      if (this.regionFilter.length > 0) {
        for (let r of this.regionFilter) {
          if (this.regionForm.get(r) != null)
            this.regionForm.push(new FormControl(r))
        }
      }
    }

    this.openFilter = !this.openFilter
  }

  selectCategoryCheckbox(value: string): boolean {
    if (this.categoryFilter.includes(value))
      return true
    return false
  }

  selectRegionCheckbox(value: string): boolean {
    if (this.regionFilter.includes(value))
      return true
    return false
  }

  checkCategory(value: string): void {
    if (value) {
      this.categoryForm.push(new FormControl(value))

      if (!this.categoryFilter.includes(value)) {
        this.categoryFilter.push(value)
      } else {
        const idx = this.categoryFilter.indexOf(value)
        this.categoryFilter.splice(idx, 1)
      }

    } else {
      let i: number = 0
      this.categoryForm.controls.forEach((ctr: AbstractControl<any, any>) => {
        if (ctr.value == value) {
          this.categoryForm.removeAt(i)
          return
        }
        i++
      })
    }
  }

  checkRegion(value: string): void {
    if (value) {
      this.regionForm.push(new FormControl(value))

      if (!this.regionFilter.includes(value)) {
        this.regionFilter.push(value)
      } else {
        const idx = this.regionFilter.indexOf(value)
        this.regionFilter.splice(idx, 1)
      }

    } else {
      let i: number = 0
      this.regionForm.controls.forEach((ctr: AbstractControl<any, any>) => {
        if (ctr.value == value) {
          this.regionForm.removeAt(i)
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
          let keyword: string = ''
          if (value != null)
            keyword = value.toLowerCase()
          return this.bizSvc.autocompleteKeyword(keyword);
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

    const category: string[] = this.categoryFilter
    const region: string[] = this.regionFilter
    // const category: string[] = this.searchForm.value['category']
    // const region: string[] = this.searchForm.value['region']

    const searchTerms: Search = { keyword, category, region }

    this.sub$ = this.bizSvc.getBusinessesByKeyword(searchTerms)
      .subscribe({
        next: () => {
          this.search.next(searchTerms)
          this.resetForm(formDirective)
        },
        error: () => {
          alert('No results found based on your current selection')
        }
      })
  }

  resetForm(formDirective: FormGroupDirective): void {
    this.createForm()
    formDirective.resetForm()
    this.categoryFilter = []
    this.regionFilter = []
    this.changeFilter()
  }
  ngOnDestroy(): void {
    if (this.sub$ !== undefined)
    this.sub$.unsubscribe()
  }
}
