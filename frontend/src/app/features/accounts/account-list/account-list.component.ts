import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AccountService } from '../../../core/services/account.service';
import { CustomerService } from '../../../core/services/customer.service';
import { BankAccount, Customer } from '../../../core/models/models';

@Component({
  selector: 'app-account-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
  <div class="card">
    <h3>Open a new account</h3>
    <div class="row wrap">
      <select class="input" [(ngModel)]="newCustomerId">
        <option [ngValue]="undefined" disabled>Select customer</option>
        <option *ngFor="let c of customers" [ngValue]="c.id">{{c.name}}</option>
      </select>
      <select class="input" [(ngModel)]="newType">
        <option value="current">Current</option>
        <option value="saving">Saving</option>
      </select>
      <input class="input" type="number" [(ngModel)]="initialBalance" placeholder="Initial balance"/>
      <input class="input" type="number" [(ngModel)]="extra" [placeholder]="newType==='current' ? 'OverDraft' : 'Interest rate %'"/>
      <button class="btn primary" (click)="create()">Create</button>
    </div>
  </div>

  <div class="card">
    <h3>Accounts</h3>
    <table class="table">
      <thead><tr><th>ID</th><th>Type</th><th>Balance</th><th>Owner</th><th>Status</th><th></th></tr></thead>
      <tbody>
        <tr *ngFor="let a of accounts">
          <td class="mono">{{a.id.slice(0,8)}}…</td>
          <td><span class="badge">{{a.type}}</span></td>
          <td>{{a.balance | number:'1.2-2'}} {{a.currency}}</td>
          <td>{{a.customerDTO?.name}}</td>
          <td>{{a.status}}</td>
          <td><a class="btn ghost" [routerLink]="['/accounts', a.id]">Operations</a></td>
        </tr>
      </tbody>
    </table>
  </div>`
})
export class AccountListComponent implements OnInit {
  accounts: BankAccount[] = []; customers: Customer[] = [];
  newCustomerId?: number; newType = 'current'; initialBalance = 1000; extra = 0;
  constructor(private svc: AccountService, private custSvc: CustomerService) {}
  ngOnInit() { this.load(); this.custSvc.getAll().subscribe(c => this.customers = c); }
  load() { this.svc.getAll().subscribe(a => this.accounts = a); }
  create() {
    if (!this.newCustomerId) return;
    const obs = this.newType === 'current'
      ? this.svc.createCurrent(this.newCustomerId, this.initialBalance, this.extra)
      : this.svc.createSaving(this.newCustomerId, this.initialBalance, this.extra);
    obs.subscribe(() => this.load());
  }
}
