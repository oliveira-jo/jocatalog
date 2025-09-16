
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { NgIf } from '@angular/common';
import { ProductService } from '../../../../services/product.service';
import { product } from '../../../../models/product';
import { CategoryService } from '../../../../services/category.service';
import { category } from '../../../../models/category';

@Component({
  selector: 'app-product-update',
  imports: [
    NgIf,
    ReactiveFormsModule
  ],
  providers: [
    ProductService
  ],
  templateUrl: './product-update.component.html',
  styleUrl: './product-update.component.css'
})
export class ProductUpdateComponent implements OnInit, OnDestroy {

  errorMessage: string = '';
  formMode!: string;
  product!: product;
  productForm!: FormGroup;
  private subscription!: Subscription;
  categories!: category[];
  selectedCategoryId!: number;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private service: ProductService,
    private categoryService: CategoryService
  ) {
  }

  ngOnInit() {
    this.formMode = 'new';
    this.getCategories();
    this.productForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      description: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(1000)]],
      imgUrl: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(500)]],
      price: ['', [Validators.required]],
    });

    this.subscription = this.route.paramMap.subscribe(
      params => {
        const id = params.get('id');
        const name = params.get('name');
        const description = params.get('description');
        const price = params.get('price');
        const imgUrl = params.get('imgUrl');
        const categories = params.get('category');
        if (id == null || id == '') {
          const prod: product = {
            id: '', name: '', description: '', imgUrl: '', price: null, categories: null
          }
          this.showProduct(prod);
        } else {
          this.getProduct(id);
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

  getProduct(id: string): void {
    this.service.getProduct(id).subscribe(
      (product: product) => this.showProduct(product),
      (error: any) => this.errorMessage = <any>error
    )
  }

  showProduct(prod: product): void {
    if (this.productForm) {
      this.productForm.reset();
    }

    this.product = prod;
    this.productForm.patchValue({
      name: this.product.name,
      description: this.product.description,
      price: this.product.price,
      imgUrl: this.product.imgUrl
    });
  }

  deleteProduct(): void {
    if (this.product.id == '') {
      this.onSaveComplete();
    } else {
      if (confirm(`Tem certeza que deseja excluir o produto: ${this.product.name} ?`)) {
        this.service.delete(this.product.id!).subscribe(
          () => this.onSaveComplete(),
          (error: any) => this.errorMessage = <any>error
        );
      }
    }
  }

  saveProduct(): void {

    if (this.productForm.valid) {
      if (this.productForm.dirty) {

        // PUT METHOD
        const newProduct = { ...this.product, ...this.productForm.value };
        this.service.update(newProduct).subscribe(
          () => this.onSaveComplete(),
          (error: any) => this.errorMessage = <any>error
        );

      } else {
        this.onSaveComplete();
      }
    } else {
      this.errorMessage = 'Por favor, corrija os erros de validação.';
    }
  }

  onSaveComplete(): void {
    this.productForm.reset();
    this.router.navigate(['/products-painel']);
  }

  closeAlert() {
    this.errorMessage = '';
  }

  cancel() {
    this.router.navigate(['/products-painel'])
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}
