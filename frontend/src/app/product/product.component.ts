import { Component, OnInit } from '@angular/core';
import { ProductService } from '../services/product.service';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ProductListComponent } from '../product-list/product-list.component';
import { ProductCreateComponent } from '../product-create/product-create.component';

@Component({
  selector: 'app-product',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    RouterModule,
    ProductListComponent,
    ProductCreateComponent,
  ],
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css'],
})
export class ProductComponent {
  // Variable pour gérer l'onglet sélectionné
  selectedTab: 'list' | 'create' = 'list'; // Onglet par défaut est 'list'
  
}
