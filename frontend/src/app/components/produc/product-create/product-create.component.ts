import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { NgIf } from '@angular/common';

import { ProductService } from '../../../services/product.service';
import { product } from '../../../models/product';

@Component({
  selector: 'app-product-create',
  imports: [
    NgIf,
    ReactiveFormsModule,
    RouterLink
  ],
  providers: [
    ProductService
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

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private service: ProductService
  ) {

  }

  ngOnInit() {
    this.formMode = 'new';
    this.productForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      description: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(1000)]],
      imgUrl: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      price: ['', [Validators.required]]
    });

    this.subscription = this.route.paramMap.subscribe(
      params => {
        const id = params.get('id');
        const name = params.get('name');
        const description = params.get('description');
        const price = params.get('price');
        const imgUrl = params.get('imgUrl');
        const categories = params.get('categories');

        if (id == null || id == '') {
          const t: product = {
            id: '', name: '', description: '', imgUrl: '', price: null, categories: []
          }
        }

      }
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  saveProduct(): void {
    if (this.productForm.valid) { // form validation
      if (this.productForm.dirty) { // method was modify from the beginning

        const newProduct = { ...this.product, ...this.productForm.value };
        this.service.create(newProduct).subscribe( // POST
          () => this.router.navigate(['/products']),
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

  closeAlert() {
    this.errorMessage = '';
  }

}
