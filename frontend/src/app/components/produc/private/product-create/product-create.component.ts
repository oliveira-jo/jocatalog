import { FormBuilder, FormGroup, NgModel, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { NgIf } from '@angular/common';

import { ProductService } from '../../../../services/product.service';
import { product } from '../../../../models/product';
import { CategoryService } from '../../../../services/category.service';
import { category } from '../../../../models/category';


@Component({
  selector: 'app-product-create',
  imports: [
    NgIf,
    ReactiveFormsModule
  ],
  providers: [
    ProductService,
    CategoryService,
    NgModel
  ],
  templateUrl: './product-create.component.html',
  styleUrl: './product-create.component.css'
})
export class ProductCreateComponent implements OnInit, OnDestroy {

  errorMessage: string = '';
  formMode!: string;
  product!: product;
  productForm!: FormGroup;
  private subscription!: Subscription

  categories!: category[];
  selectedCategoryId!: number;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private service: ProductService,
    private categoryService: CategoryService
  ) { }

  ngOnInit() {
    this.formMode = 'new';
    this.getCategories();
    this.productForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      description: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(1000)]],
      imgUrl: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      price: ['', [Validators.required]],
    });

    this.subscription = this.route.paramMap.subscribe(
      params => {
        const id = params.get('id');
        const name = params.get('name');
        const description = params.get('description');
        const price = params.get('price');
        const imgUrl = params.get('imgUrl');
        const category = params.get('category');

        if (id == null || id == '') {
          const prod: product = {
            id: '', name: '', description: '', imgUrl: '', price: null, categories: null
          }
        }

      }
    );
  }

  onCategoryChange(event: any) {
    this.selectedCategoryId = event.target.value;
  }

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

  saveProduct(): void {
    if (this.productForm.valid) {
      if (this.productForm.dirty) {

        const newProduct = { ...this.product, ...this.productForm.value };

        this.service.create(newProduct).subscribe( // POST
          () => this.router.navigate(['/products-painel']),
          (error: any) => {
            this.errorMessage = <any>error;
            this.errorMessage = 'Erro ao cadastrar produto - product.';
          }

        );
      }
    } else {
      this.errorMessage = 'Por favor, corrija os erros de validação.';
    }
  }

  cancel() {
    this.router.navigate(['/products-painel'])
  }

  closeAlert() {
    this.errorMessage = '';
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}
