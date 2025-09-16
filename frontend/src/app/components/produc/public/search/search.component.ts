import { Component } from '@angular/core';
import { ProductListComponent } from "../product-list/product-list.component";

@Component({
  selector: 'app-search',
  imports: [ProductListComponent],
  templateUrl: './search.component.html',
  styleUrl: './search.component.css'
})
export class SearchComponent {

}
