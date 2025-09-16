import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProductService } from '../../../../services/product.service';
import { product } from '../../../../models/product';

@Component({
  selector: 'app-product-list',
  imports: [
    CommonModule,
    RouterLink
  ],
  providers: [
    ProductService
  ],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent implements OnInit {

  products: product[] | undefined;
  errorMessage: string = '';

  constructor(private service: ProductService) { }

  ngOnInit(): void {
    this.getProducts();
  }

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

  onSaveComplete() {
    this.service.getProducts().subscribe(
      produc => {
        this.products = this.products;
      },
      error => this.errorMessage = <any>error
    );
  }

}
