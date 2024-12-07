import { Routes } from '@angular/router';
import { UserComponent } from './user/user.component';
import { ProductComponent } from './product/product.component';
import { LoginComponent } from './login/login.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { HomeComponent } from './home/home.component';

export const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'profil', component: UserComponent },
  { path: 'products', component: ProductComponent },
  { path: 'product/:id', component: ProductDetailComponent },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
];
