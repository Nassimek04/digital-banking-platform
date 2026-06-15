import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <div class="card" style="max-width:420px">
    <h3>Change Password</h3>
    <input class="input" type="password" [(ngModel)]="oldPassword" placeholder="Current password"/>
    <input class="input" type="password" [(ngModel)]="newPassword" placeholder="New password"/>
    <button class="btn primary" (click)="submit()">Update</button>
    <p class="success" *ngIf="message">{{ message }}</p>
    <p class="error" *ngIf="error">{{ error }}</p>
  </div>`
})
export class ChangePasswordComponent {
  oldPassword=''; newPassword=''; message=''; error='';
  constructor(private auth: AuthService) {}
  submit() {
    this.message=''; this.error='';
    this.auth.changePassword(this.oldPassword, this.newPassword).subscribe({
      next: () => { this.message='Password updated successfully'; this.oldPassword=''; this.newPassword=''; },
      error: (e) => this.error = e?.error?.error || 'Failed to change password'
    });
  }
}
