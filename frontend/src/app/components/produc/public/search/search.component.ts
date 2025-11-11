import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../../services/product.service';
import { Product } from '../../../../models/product';
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

  products: Product[] = [];
  totalPages: number = 0;
  currentPage: number = 0;
  pages: number[] = [];

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
    this.loadPage(0);
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
        this.products = products.content.map(p => ({
          ...((p as any)),
          id: Number((p as any).id ?? 0)
        })) as Product[];
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

  loadPage(page: number): void {
    this.service.getProducts(page, 6).subscribe(
      response => {
        this.products = response.content;
        this.totalPages = response.totalPages;
        this.currentPage = response.number;
        this.pages = Array.from({ length: this.totalPages }, (_, i) => i);
      });
  }

  onCategoryChange(event: any) {
    this.selectedCategoryId = event.target.value;
  }

  onSaveComplete(page: number) {
    this.service.getProducts(page, 6).subscribe(
      prod => {
        this.products = prod.content;
      },
      error => this.errorMessage = <any>error
    );
  }

}
