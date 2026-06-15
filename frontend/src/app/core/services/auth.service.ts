import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'access-token';

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<{ 'access-token': string }> {
    return this.http
      .post<{ 'access-token': string }>(`${environment.apiUrl}/auth/login`, { username, password })
      .pipe(tap(res => localStorage.setItem(this.tokenKey, res['access-token'])));
  }

  changePassword(oldPassword: string, newPassword: string): Observable<any> {
    return this.http.post(`${environment.apiUrl}/auth/change-password`, { oldPassword, newPassword });
  }

  logout(): void { localStorage.removeItem(this.tokenKey); }
  getToken(): string | null { return localStorage.getItem(this.tokenKey); }
  isAuthenticated(): boolean { return !!this.getToken(); }

  get username(): string {
    const token = this.getToken();
    if (!token) return '';
    try { return JSON.parse(atob(token.split('.')[1])).sub; } catch { return ''; }
  }

  get roles(): string[] {
    const token = this.getToken();
    if (!token) return [];
    try { return (JSON.parse(atob(token.split('.')[1])).scope || '').split(' '); } catch { return []; }
  }

  isAdmin(): boolean { return this.roles.includes('ADMIN'); }
}
