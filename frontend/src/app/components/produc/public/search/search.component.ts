import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../../services/product.service';
import { product } from '../../../../models/product';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CategoryService } from '../../../../services/category.service';
import { category } from '../../../../models/category';
import { Subscription } from 'rxjs';
import { ActivatedRoute, RouterLink } from '@angular/router';

@Component({
  selector: 'app-search',
  imports: [
    ReactiveFormsModule,
    RouterLink
  ],
  providers: [
    ProductService,
    CategoryService
  ],
  templateUrl: './search.component.html',
  styleUrl: './search.component.css'
})
export class SearchComponent implements OnInit {

  products: product[] | undefined;
  errorMessage: string = '';
  categories!: category[];
  selectedCategoryId!: number;

  formMode!: string;
  searchForm!: FormGroup;
  private subscription!: Subscription

  categoryName!: string | null;
  categoryId!: string | null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private service: ProductService,
    private categoryService: CategoryService
  ) { }

  ngOnInit(): void {
    this.getCategories();

    // build reactive form
    this.searchForm = this.fb.group({
      name: [''],
      category: ['']
    });

    // if route params exist, preload them
    this.route.paramMap.subscribe(params => {
      this.categoryName = params.get('name');
      this.categoryId = params.get('category');

      // update the form with params
      this.searchForm.patchValue({
        name: this.categoryName,
        category: this.categoryId
      });
    });
  }

  searchProduct() {

    const pesquisaNome = this.searchForm.get('name')?.value;
    const category = this.searchForm.get('category')?.value;

    this.service.searchProduct(pesquisaNome, category).subscribe({
      next: (products) => {
        this.products = products.content;
      },
      error: (err) => {
        this.errorMessage = <any>err
      }
    })
  };

  getCategories() {
    this.categoryService.getCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: (err) => {
        this.errorMessage = <any>err
      }
    })
  };

  getProducts() {
    this.service.getProducts().subscribe({
      next: (products) => {
        this.products = products.content;
      },
      error: (err) => {
        this.errorMessage = <any>err
      }
    })
  };

  onCategoryChange(event: any) {
    this.selectedCategoryId = event.target.value;
  }

  onSaveComplete() {
    this.service.getProducts().subscribe(
      prod => {
        this.products = prod.content;
      },
      error => this.errorMessage = <any>error
    );
  }

}
