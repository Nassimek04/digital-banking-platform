import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private base = `${environment.apiUrl}/dashboard`;
  constructor(private http: HttpClient) {}

  stats(): Observable<any> { return this.http.get(`${this.base}/stats`); }
  accountTypes(): Observable<any> { return this.http.get(`${this.base}/accountTypes`); }
  operationsByType(): Observable<any> { return this.http.get(`${this.base}/operationsByType`); }
  balanceByCustomer(): Observable<any> { return this.http.get(`${this.base}/balanceByCustomer`); }
}
