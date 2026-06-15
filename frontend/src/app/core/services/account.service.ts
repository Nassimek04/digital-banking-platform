import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { BankAccount, AccountHistory } from '../models/models';

@Injectable({ providedIn: 'root' })
export class AccountService {
  private base = `${environment.apiUrl}/accounts`;
  constructor(private http: HttpClient) {}

  getAll(): Observable<BankAccount[]> { return this.http.get<BankAccount[]>(this.base); }
  getOne(id: string): Observable<BankAccount> { return this.http.get<BankAccount>(`${this.base}/${id}`); }
  getByCustomer(customerId: number): Observable<BankAccount[]> {
    return this.http.get<BankAccount[]>(`${this.base}/customer/${customerId}`);
  }
  history(id: string, page: number, size: number): Observable<AccountHistory> {
    return this.http.get<AccountHistory>(`${this.base}/${id}/pageOperations?page=${page}&size=${size}`);
  }
  createCurrent(customerId: number, initialBalance: number, overDraft: number): Observable<BankAccount> {
    return this.http.post<BankAccount>(`${this.base}/current/${customerId}?initialBalance=${initialBalance}&overDraft=${overDraft}`, {});
  }
  createSaving(customerId: number, initialBalance: number, interestRate: number): Observable<BankAccount> {
    return this.http.post<BankAccount>(`${this.base}/saving/${customerId}?initialBalance=${initialBalance}&interestRate=${interestRate}`, {});
  }
  debit(accountId: string, amount: number, description: string): Observable<any> {
    return this.http.post(`${this.base}/debit`, { accountId, amount, description });
  }
  credit(accountId: string, amount: number, description: string): Observable<any> {
    return this.http.post(`${this.base}/credit`, { accountId, amount, description });
  }
  transfer(accountSource: string, accountDestination: string, amount: number): Observable<any> {
    return this.http.post(`${this.base}/transfer`, { accountSource, accountDestination, amount });
  }
}
