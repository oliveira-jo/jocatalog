
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProductService } from '../../../../services/product.service';
import { Product } from '../../../../models/product';
@Component({
  selector: 'app-product-list-admin',
  imports: [
    CommonModule,
    RouterLink
  ],
  providers: [
    ProductService
  ],
  templateUrl: './product-list-admin.component.html',
  styleUrl: './product-list-admin.component.css'
})
export class ProductListAdminComponent implements OnInit {

  //products: Product[] | undefined;
  errorMessage: string = '';

  products: Product[] = [];
  totalPages: number = 0;
  currentPage: number = 0;
  pages: number[] = [];

  constructor(private service: ProductService) { }

  ngOnInit(): void {
    this.loadPage(0);
  }

  loadPage(page: number): void {
    this.service.getProducts(page, 6).subscribe(
      response => {
        this.products = response.content;
        this.totalPages = response.totalPages;
        this.currentPage = response.number;
        this.pages = Array.from({ length: this.totalPages }, (_, i) => i);
      });
  }

  deleteProduct(id: string): void {
    if (id == '') {
      this.onSaveComplete(0);
    } else {
      if (confirm(`Tem certeza que deseja excluir o ticket: ?`)) {
        this.service.delete(id)
          .subscribe(
            () => this.onSaveComplete(0),
            (error: any) => this.errorMessage = <any>error
          );
      }
    }
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
