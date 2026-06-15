import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../core/services/auth.service';
import { ChatbotComponent } from '../features/chatbot/chatbot.component';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, ChatbotComponent],
  template: `
  <div class="shell">
    <aside class="sidebar">
      <div class="brand">🏦 Digital Banking</div>
      <nav>
        <a routerLink="/dashboard" routerLinkActive="active">Dashboard</a>
        <a routerLink="/customers" routerLinkActive="active">Customers</a>
        <a routerLink="/accounts" routerLinkActive="active">Accounts</a>
        <a routerLink="/change-password" routerLinkActive="active">Change Password</a>
      </nav>
    </aside>
    <div class="main">
      <header class="topbar">
        <span></span>
        <div class="user">
          <span>{{ auth.username }}</span>
          <span class="badge" *ngIf="auth.isAdmin()">ADMIN</span>
          <button class="btn ghost" (click)="logout()">Logout</button>
        </div>
      </header>
      <section class="content"><router-outlet></router-outlet></section>
    </div>
    <app-chatbot></app-chatbot>
  </div>`,
  styles: [`
    .shell{display:flex;min-height:100vh}
    .sidebar{width:230px;background:#0f172a;color:#fff;padding:20px 0}
    .brand{font-weight:700;padding:0 20px 20px}
    nav{display:flex;flex-direction:column}
    nav a{color:#cbd5e1;padding:12px 20px;text-decoration:none}
    nav a.active,nav a:hover{background:#1e293b;color:#fff;border-left:3px solid #3b82f6}
    .main{flex:1;background:#f1f5f9}
    .topbar{display:flex;justify-content:space-between;align-items:center;background:#fff;padding:12px 24px;border-bottom:1px solid #e2e8f0}
    .user{display:flex;gap:10px;align-items:center}
    .content{padding:24px}
    .badge{background:#dbeafe;color:#1e40af;padding:2px 8px;border-radius:8px;font-size:12px}
  `]
})
export class MainLayoutComponent {
  constructor(public auth: AuthService, private router: Router) {}
  logout() { this.auth.logout(); this.router.navigate(['/login']); }
}
