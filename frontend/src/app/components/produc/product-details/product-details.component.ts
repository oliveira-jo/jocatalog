import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { product } from '../../../models/product';
import { Subscription } from 'rxjs';
import { ProductService } from '../../../services/product.service';

@Component({
  selector: 'app-product-details',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
  ],
  templateUrl: './product-details.component.html',
  styleUrl: './product-details.component.css'
})
export class ProductDetailsComponent implements OnInit {

  errorMessage: string = '';
  product!: product;
  private subscription!: Subscription

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: ProductService

  ) { }

  ngOnInit(): void {
    this.subscription = this.route.paramMap.subscribe(
      params => {
        const id = params.get('id');
        if (id == null || id == '') {
          this.router.navigate(['/tickets'])
        } else {
          this.getProducts(id);
        }
      }
    );
  }

  getProducts(id: string): void {
    this.service.getProduct(id).subscribe(
      (product: product) => {
        this.product = product
      },
      (error: any) => this.errorMessage = <any>error
    )
  }

  deleteProduct(): void {
    if (this.product.id == '') {
      this.errorMessage = 'Erro ao tentar deletar ticket!';
    } else {
      if (confirm(`Tem certeza que deseja excluir o ticket: ${this.product.name} ?`)) {
        this.service.delete(this.product.id!).subscribe(
          () => this.onSaveComplete(),
          (error: any) => this.errorMessage = <any>error
        );
      }
    }
  }

  onSaveComplete(): void {
    this.router.navigate(['/products']);
  }

  closeAlert() {
    this.errorMessage = '';
  }

}
