import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Customer } from '../models/models';

@Injectable({ providedIn: 'root' })
export class CustomerService {
  private base = `${environment.apiUrl}/customers`;
  constructor(private http: HttpClient) {}

  getAll(): Observable<Customer[]> { return this.http.get<Customer[]>(this.base); }
  search(keyword: string): Observable<Customer[]> {
    return this.http.get<Customer[]>(`${this.base}/search?keyword=${keyword}`);
  }
  getOne(id: number): Observable<Customer> { return this.http.get<Customer>(`${this.base}/${id}`); }
  create(c: Customer): Observable<Customer> { return this.http.post<Customer>(this.base, c); }
  update(id: number, c: Customer): Observable<Customer> { return this.http.put<Customer>(`${this.base}/${id}`, c); }
  delete(id: number): Observable<void> { return this.http.delete<void>(`${this.base}/${id}`); }
}
