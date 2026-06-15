import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration } from 'chart.js';
import { DashboardService } from '../../core/services/dashboard.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, BaseChartDirective],
  template: `
  <div class="stats">
    <div class="stat card"><span class="num">{{stats?.totalCustomers}}</span><span>Customers</span></div>
    <div class="stat card"><span class="num">{{stats?.totalAccounts}}</span><span>Accounts</span></div>
    <div class="stat card"><span class="num">{{stats?.totalOperations}}</span><span>Operations</span></div>
    <div class="stat card"><span class="num">{{stats?.totalBalance | number:'1.0-0'}}</span><span>Total balance (MAD)</span></div>
  </div>

  <div class="grid">
    <div class="card"><h4>Account types</h4>
      <canvas baseChart [data]="pieData" [type]="'pie'"></canvas></div>
    <div class="card"><h4>Operations by type</h4>
      <canvas baseChart [data]="opData" [type]="'doughnut'"></canvas></div>
    <div class="card wide"><h4>Balance by customer</h4>
      <canvas baseChart [data]="barData" [type]="'bar'" [options]="barOptions"></canvas></div>
  </div>`,
  styles: [`
    .stats{display:grid;grid-template-columns:repeat(4,1fr);gap:16px;margin-bottom:16px}
    .stat{display:flex;flex-direction:column;align-items:center;padding:18px}
    .stat .num{font-size:28px;font-weight:700;color:#1e3a8a}
    .grid{display:grid;grid-template-columns:1fr 1fr;gap:16px}
    .wide{grid-column:1 / span 2}
    canvas{max-height:300px}
  `]
})
export class DashboardComponent implements OnInit {
  stats: any;
  pieData: ChartConfiguration['data'] = { labels: [], datasets: [{ data: [] }] };
  opData: ChartConfiguration['data'] = { labels: [], datasets: [{ data: [] }] };
  barData: ChartConfiguration['data'] = { labels: [], datasets: [{ data: [], label: 'Balance' }] };
  barOptions: ChartConfiguration['options'] = { responsive: true, plugins: { legend: { display: false } } };

  constructor(private svc: DashboardService) {}
  ngOnInit() {
    this.svc.stats().subscribe(s => this.stats = s);
    this.svc.accountTypes().subscribe(d => this.pieData = {
      labels: Object.keys(d),
      datasets: [{ data: Object.values(d), backgroundColor: ['#3b82f6', '#10b981'] }]
    });
    this.svc.operationsByType().subscribe(d => this.opData = {
      labels: Object.keys(d),
      datasets: [{ data: Object.values(d), backgroundColor: ['#22c55e', '#ef4444'] }]
    });
    this.svc.balanceByCustomer().subscribe(d => this.barData = {
      labels: Object.keys(d),
      datasets: [{ data: Object.values(d), label: 'Balance', backgroundColor: '#6366f1' }]
    });
  }
}
