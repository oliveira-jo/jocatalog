
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProductService } from '../../../services/product.service';
import { product } from '../../../models/product';
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

  deleteProduct(id: string): void {
    if (id == '') {
      this.onSaveComplete();
    } else {
      if (confirm(`Tem certeza que deseja excluir o ticket: ?`)) {
        this.service.delete(id)
          .subscribe(
            () => this.onSaveComplete(),
            (error: any) => this.errorMessage = <any>error
          );
      }
    }
  }

  onSaveComplete() {
    this.service.getProducts().subscribe(
      tickets => {
        this.products = this.products;
      },
      error => this.errorMessage = <any>error
    );
  }

}
