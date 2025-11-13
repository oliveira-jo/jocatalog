
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProductService } from '../../../../services/product.service';
import { Product } from '../../../../models/product';
import { AuthService } from '../../../../services/auth.service';
import { user } from '../../../../models/user';
@Component({
  selector: 'app-product-list-admin',
  standalone: true,
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

  products: Product[] = [];
  totalPages: number = 0;
  currentPage: number = 0;
  pages: number[] = [];
  errorMessage: string = '';

  constructor(private service: ProductService, private authService: AuthService) { }

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
