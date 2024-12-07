import { Component } from '@angular/core';
import { ProductService } from '../services/product.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-product-create',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './product-create.component.html',
  styleUrl: './product-create.component.css',
})
export class ProductCreateComponent {
  products: any[] = [];
  product = { name: '', price: 0, expirationDate: '' };

  constructor(private productService: ProductService, private router: Router) {}

  ngOnInit() {
    this.loadProducts();
  }

  loadProducts() {
    this.productService.getProducts().subscribe((data) => {
      this.products = data;
    });
  }

  addProduct() {
    const productToAdd = { ...this.product, description: '' };
    this.productService.addProduct(productToAdd).subscribe(() => {
      this.loadProducts();
      this.product = { name: '', price: 0, expirationDate: '' };
    });
  }
}
