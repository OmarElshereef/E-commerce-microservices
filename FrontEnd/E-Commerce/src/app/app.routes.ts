import { Routes } from '@angular/router';
import { ProductsList } from './pages/products-list/products-list';
import { Cart } from './pages/cart/cart';
import { Auth } from './pages/auth/auth';

export const routes: Routes = [
  {
    path: 'auth',
    component: Auth
  },
  {
    path: '',
    pathMatch: 'full',
    component: ProductsList,
    //canActivate: [AuthGuard]   // 👈 protect
  },
  {
    path: 'cart',
    component: Cart,
    //canActivate: [AuthGuard]   // 👈 protect
  }
];
