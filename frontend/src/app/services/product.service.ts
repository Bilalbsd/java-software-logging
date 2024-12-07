import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { trace } from '@opentelemetry/api';
import { tap } from 'rxjs/operators';

// Définir une interface pour le produit
interface Product {
  id?: string;
  name: string;
  description: string;
  price: number;
  // Ajoutez d'autres propriétés en fonction des besoins
}

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiUrl = 'http://localhost:8080/product';

  constructor(private http: HttpClient) {}

  // Obtenir la liste des produits
  getProducts(): Observable<Product[]> {
    const span = trace.getTracer('frontend_service').startSpan('getProducts');
    span.setAttribute('service.name', 'frontend_service');
    span.setAttribute('frontend.operation', 'fetchProducts');
    return this.http.get<Product[]>(this.apiUrl).pipe(
      tap(() => span.end()) // Terminez le span après la requête
    );
  }

  // Ajouter un produit
  addProduct(product: Product): Observable<Product> {
    const span = trace.getTracer('frontend_service').startSpan('addProduct');
    span.setAttribute('frontend.operation', 'addProduct');
    return this.http.post<Product>(this.apiUrl, product).pipe(
      tap(() => span.end()) // Terminez le span après la requête
    );
  }

  // Supprimer un produit par ID
  deleteProduct(id: string): Observable<void> {
    const span = trace.getTracer('frontend_service').startSpan('deleteProduct');
    span.setAttribute('frontend.operation', 'deleteProduct');
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      tap(() => span.end()) // Terminez le span après la requête
    );
  }

  // Obtenir un produit par son ID
  getProductById(id: string): Observable<Product> {
    const span = trace
      .getTracer('frontend_service')
      .startSpan('getProductById');
    span.setAttribute('frontend.operation', 'fetchProductById');
    return this.http.get<Product>(`${this.apiUrl}/${id}`).pipe(
      tap(() => span.end()) // Terminez le span après la requête
    );
  }
}
