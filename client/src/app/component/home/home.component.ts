import { Component, OnDestroy, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClientService } from 'src/app/service/client.service';
import { Subject, Subscription, switchMap } from 'rxjs';
import { Router } from '@angular/router';
import { Search } from 'src/app/interface/Search';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

  selectedCategory: string = ''
  searchTerms: Search = {}

  changeCategory(category: string) {
    this.selectedCategory = category
  }

  searchKeyword(search: Search) {
    this.searchTerms = search
  }
}
