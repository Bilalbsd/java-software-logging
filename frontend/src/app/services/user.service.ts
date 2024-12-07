import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { trace } from '@opentelemetry/api';
import { tap } from 'rxjs/operators';

interface User {
  name: string;
  age: number;
  email: string;
  password: string;
}

interface UserResponse {
  id: string;
  name: string;
  age: number;
  email: string;
  password: string;
}

@Injectable({
  providedIn: 'root',
})
export class UserService {
  // URL de l'API
  private apiUrl = 'http://localhost:8080/user';

  constructor(private http: HttpClient) {}

  // Méthode d'enregistrement d'un utilisateur
  register(user: User): Observable<any> {
    const span = trace.getTracer('frontend_service').startSpan('registerUser');
    span.setAttribute('frontend.operation', 'registerUser');
    return this.http.post(`${this.apiUrl}/register`, user).pipe(
      tap(() => span.end()) // Terminez le span après la requête
    );
  }

  // Méthode de connexion
  login(email: string, password: string): Observable<UserResponse> {
    const span = trace.getTracer('frontend_service').startSpan('loginUser');
    span.setAttribute('frontend.operation', 'loginUser');
    return this.http
      .post<UserResponse>(`${this.apiUrl}/login`, {
        email,
        password,
      })
      .pipe(
        tap(() => span.end()) // Terminez le span après la requête
      );
  }

  // Méthode de déconnexion
  logout(): Observable<any> {
    const span = trace.getTracer('frontend_service').startSpan('logoutUser');
    span.setAttribute('frontend.operation', 'logoutUser');
    // Suppression de l'utilisateur du localStorage
    localStorage.removeItem('user');

    // Appel HTTP pour déconnexion (on suppose que l'API répond à ce point)
    return this.http.get(`${this.apiUrl}/logout`).pipe(
      tap(() => span.end()) // Terminez le span après la requête
    );
  }
}
