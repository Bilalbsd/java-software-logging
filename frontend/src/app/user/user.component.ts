import { Component } from '@angular/core';
import { UserService } from '../services/user.service';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClient } from '@angular/common/http';

// Création d'une interface pour l'utilisateur
interface User {
  id: string;
  name: string;
  age: number;
  email: string;
  password: string;
}

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css'],
})
export class UserComponent {
  constructor(private userService: UserService, private router: Router) {}

  userInfos: User = JSON.parse(localStorage.getItem('user')!);

  logout(): void {
    this.userService.logout();
    this.router.navigate(['/login']);
  }
}
