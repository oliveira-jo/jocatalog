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

  products: Product[] = [];
  totalPages: number = 0;
  currentPage: number = 0;
  pages: number[] = [];

  errorMessage: string = '';

  constructor(private service: ProductService) { }

  ngOnInit(): void {
    this.loadProducts(0);
  }

  loadProducts(page: number): void {
    this.service.getProducts(page, 6).subscribe(
      response => {
        this.products = response.content;
        this.totalPages = response.totalPages;
        this.currentPage = response.number;
        this.pages = Array.from({ length: this.totalPages }, (_, i) => i);

        window.scrollTo({ top: 0, behavior: 'smooth' });
      });
  }

  onSaveComplete(page: number) {
    this.service.getProducts(page, 5).subscribe(
      produc => {
        this.products = this.products;
      },
      error => this.errorMessage = <any>error
    );
  }

}
