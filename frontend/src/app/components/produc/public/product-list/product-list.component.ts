import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProductService } from '../../../../services/product.service';
import { Product } from '../../../../models/product';

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

  products: Product[] | undefined;
  errorMessage: string = '';

  constructor(private service: ProductService) { }

  ngOnInit(): void {
    this.getProducts(0);
  }

  getProducts(page: number) {
    this.service.getProducts(page, 5).subscribe({
      next: (products) => {
        this.products = products.content;
      },
      error: (err) => {
        this.errorMessage = <any>err
      }
    })
  };

  onSaveComplete(page: number) {
    this.service.getProducts(page, 5).subscribe(
      produc => {
        this.products = this.products;
      },
      error => this.errorMessage = <any>error
    );
  }

}
