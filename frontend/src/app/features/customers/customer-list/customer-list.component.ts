import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CustomerService } from '../../../core/services/customer.service';
import { AuthService } from '../../../core/services/auth.service';
import { Customer } from '../../../core/models/models';

@Component({
  selector: 'app-customer-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
  <div class="card">
    <div class="row between">
      <h3>Customers</h3>
      <a class="btn primary" routerLink="/customers/new">+ New Customer</a>
    </div>
    <div class="row">
      <input class="input" [(ngModel)]="keyword" (keyup.enter)="search()" placeholder="Search by name/email"/>
      <button class="btn" (click)="search()">Search</button>
    </div>
    <table class="table">
      <thead><tr><th>ID</th><th>Name</th><th>Email</th><th>Created by</th><th></th></tr></thead>
      <tbody>
        <tr *ngFor="let c of customers">
          <td>{{c.id}}</td><td>{{c.name}}</td><td>{{c.email}}</td><td>{{c.createdBy}}</td>
          <td class="row">
            <a class="btn ghost" [routerLink]="['/customers', c.id, 'edit']">Edit</a>
            <a class="btn ghost" [routerLink]="['/accounts']" [queryParams]="{customer: c.id}">Accounts</a>
            <button class="btn danger" *ngIf="auth.isAdmin()" (click)="remove(c)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>`
})
export class CustomerListComponent implements OnInit {
  customers: Customer[] = []; keyword = '';
  constructor(private svc: CustomerService, public auth: AuthService) {}
  ngOnInit() { this.load(); }
  load() { this.svc.getAll().subscribe(d => this.customers = d); }
  search() { this.svc.search(this.keyword).subscribe(d => this.customers = d); }
  remove(c: Customer) {
    if (confirm(`Delete ${c.name}?`)) this.svc.delete(c.id!).subscribe(() => this.load());
  }
}
