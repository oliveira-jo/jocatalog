import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../../services/product.service';
import { category } from '../../../models/category';
import { CategoryService } from '../../../services/category.service';

@Component({
  selector: 'app-category-list',
  imports: [
    CommonModule
  ],
  providers: [
    ProductService
  ],
  templateUrl: './category-list.component.html',
  styleUrl: './category-list.component.css'
})
export class CategoryListComponent implements OnInit {

  categories: category[] | undefined;
  errorMessage: string = '';

  constructor(private service: CategoryService) { }

  ngOnInit(): void {
    this.getCategories();
  }

  getCategories() {
    this.service.getCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: (err) => {
        this.errorMessage = <any>err
      }
    })
  };

  onSaveComplete() {
    this.service.getCategories().subscribe(
      category => {
        this.categories = this.categories;
      },
      error => this.errorMessage = <any>error
    );
  }

}
