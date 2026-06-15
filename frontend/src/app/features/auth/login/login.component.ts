import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <div class="login-wrap">
    <div class="card login-card">
      <h2>Digital Banking</h2>
      <p class="muted">Sign in to your account</p>
      <input [(ngModel)]="username" placeholder="Username" class="input"/>
      <input [(ngModel)]="password" type="password" placeholder="Password" class="input"/>
      <button class="btn primary" (click)="login()" [disabled]="loading">{{ loading ? '...' : 'Login' }}</button>
      <p class="error" *ngIf="error">{{ error }}</p>
      <p class="muted small">Try admin/admin123 or user1/user123</p>
    </div>
  </div>`,
  styles: [`
    .login-wrap{display:flex;align-items:center;justify-content:center;min-height:100vh}
    .login-card{width:340px;display:flex;flex-direction:column;gap:12px;text-align:center}
    h2{margin:0;color:#1e3a8a}
  `]
})
export class LoginComponent {
  username = ''; password = ''; error = ''; loading = false;
  constructor(private auth: AuthService, private router: Router) {}
  login() {
    this.loading = true; this.error = '';
    this.auth.login(this.username, this.password).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: () => { this.error = 'Invalid credentials'; this.loading = false; }
    });
  }
}
