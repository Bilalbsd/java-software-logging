import { Component } from '@angular/core';
import { UserService } from '../services/user.service';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClient } from '@angular/common/http';
import { RegisterComponent } from '../register/register.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule, RegisterComponent],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  email!: string;
  password!: string;
  message!: string;

  constructor(private userService: UserService, private router: Router) {}

  selectedTab: 'login' | 'register' = 'login';

  user = { name: '', age: 0, email: '', password: '' };

  register() {
    this.userService.register(this.user).subscribe(
      (response) => {
        this.message = 'Utilisateur créé avec succès!';
        this.router.navigate(['/login']);
      },
      (error) => {
        this.message = "Erreur lors de la création de l'utilisateur.";
      }
    );
  }

  login() {
    this.userService.login(this.email, this.password).subscribe({
      next: (response) => {
        console.log('Connexion réussie !' + JSON.stringify(response.id));
        localStorage.setItem('user', JSON.stringify(response));
        this.router.navigate(['/profil']);
      },
      error: (error) => {
        this.message = 'Erreur lors de la connexion.';
      },
    });
  }
}
