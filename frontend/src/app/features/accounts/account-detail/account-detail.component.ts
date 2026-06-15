import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AccountService } from '../../../core/services/account.service';
import { AccountHistory } from '../../../core/models/models';

@Component({
  selector: 'app-account-detail',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <div class="card" *ngIf="history">
    <h3>Account {{ history.accountId.slice(0,8) }}…</h3>
    <p>Balance: <b>{{ history.balance | number:'1.2-2' }}</b></p>

    <div class="row wrap">
      <select class="input" [(ngModel)]="opType">
        <option value="CREDIT">Credit</option>
        <option value="DEBIT">Debit</option>
        <option value="TRANSFER">Transfer</option>
      </select>
      <input class="input" type="number" [(ngModel)]="amount" placeholder="Amount"/>
      <input class="input" [(ngModel)]="description" placeholder="Description"/>
      <input class="input" *ngIf="opType==='TRANSFER'" [(ngModel)]="destination" placeholder="Destination account id"/>
      <button class="btn primary" (click)="doOperation()">Apply</button>
    </div>
    <p class="error" *ngIf="error">{{ error }}</p>

    <table class="table">
      <thead><tr><th>Date</th><th>Type</th><th>Amount</th><th>By</th><th>Description</th></tr></thead>
      <tbody>
        <tr *ngFor="let o of history.accountOperationDTOS">
          <td>{{o.operationDate | date:'short'}}</td>
          <td><span class="badge" [class.credit]="o.type==='CREDIT'" [class.debit]="o.type==='DEBIT'">{{o.type}}</span></td>
          <td>{{o.amount | number:'1.2-2'}}</td>
          <td>{{o.performedBy}}</td>
          <td>{{o.description}}</td>
        </tr>
      </tbody>
    </table>

    <div class="row">
      <button class="btn ghost" [disabled]="page===0" (click)="go(page-1)">Prev</button>
      <span>Page {{page+1}} / {{history.totalPages}}</span>
      <button class="btn ghost" [disabled]="page>=history.totalPages-1" (click)="go(page+1)">Next</button>
    </div>
  </div>`,
  styles:[`.badge.credit{background:#dcfce7;color:#166534}.badge.debit{background:#fee2e2;color:#991b1b}`]
})
export class AccountDetailComponent implements OnInit {
  accountId = ''; history?: AccountHistory; page = 0; size = 5;
  opType = 'CREDIT'; amount = 0; description = ''; destination = ''; error = '';
  constructor(private svc: AccountService, private route: ActivatedRoute) {}
  ngOnInit() { this.accountId = this.route.snapshot.paramMap.get('id')!; this.go(0); }
  go(p: number) { this.page = p; this.svc.history(this.accountId, p, this.size).subscribe(h => this.history = h); }
  doOperation() {
    this.error = '';
    let obs;
    if (this.opType === 'CREDIT') obs = this.svc.credit(this.accountId, this.amount, this.description);
    else if (this.opType === 'DEBIT') obs = this.svc.debit(this.accountId, this.amount, this.description);
    else obs = this.svc.transfer(this.accountId, this.destination, this.amount);
    obs.subscribe({ next: () => this.go(0), error: e => this.error = e?.error?.error || 'Operation failed' });
  }
}
