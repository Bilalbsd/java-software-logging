import { Component } from '@angular/core';
import { UserService } from '../services/user.service';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  user = { name: '', age: 0, email: '', password: '' };
  message: string | undefined;

  constructor(private userService: UserService, private router: Router) {}

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
}
