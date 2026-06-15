import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { LoginComponent } from './features/auth/login/login.component';
import { MainLayoutComponent } from './layout/main-layout.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent) },
      { path: 'customers', loadComponent: () => import('./features/customers/customer-list/customer-list.component').then(m => m.CustomerListComponent) },
      { path: 'customers/new', loadComponent: () => import('./features/customers/customer-form/customer-form.component').then(m => m.CustomerFormComponent) },
      { path: 'customers/:id/edit', loadComponent: () => import('./features/customers/customer-form/customer-form.component').then(m => m.CustomerFormComponent) },
      { path: 'accounts', loadComponent: () => import('./features/accounts/account-list/account-list.component').then(m => m.AccountListComponent) },
      { path: 'accounts/:id', loadComponent: () => import('./features/accounts/account-detail/account-detail.component').then(m => m.AccountDetailComponent) },
      { path: 'change-password', loadComponent: () => import('./features/auth/change-password/change-password.component').then(m => m.ChangePasswordComponent) },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: '' }
];
