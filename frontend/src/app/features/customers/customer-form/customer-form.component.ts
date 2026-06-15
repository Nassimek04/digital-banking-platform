import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../../../core/services/customer.service';
import { Customer } from '../../../core/models/models';

@Component({
  selector: 'app-customer-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <div class="card" style="max-width:480px">
    <h3>{{ id ? 'Edit' : 'New' }} Customer</h3>
    <input class="input" [(ngModel)]="model.name" placeholder="Name"/>
    <input class="input" [(ngModel)]="model.email" placeholder="Email"/>
    <div class="row">
      <button class="btn primary" (click)="save()">Save</button>
      <button class="btn ghost" (click)="cancel()">Cancel</button>
    </div>
    <p class="error" *ngIf="error">{{ error }}</p>
  </div>`
})
export class CustomerFormComponent implements OnInit {
  model: Customer = { name: '', email: '' };
  id?: number; error = '';
  constructor(private svc: CustomerService, private route: ActivatedRoute, private router: Router) {}
  ngOnInit() {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) { this.id = +idParam; this.svc.getOne(this.id).subscribe(c => this.model = c); }
  }
  save() {
    const obs = this.id ? this.svc.update(this.id, this.model) : this.svc.create(this.model);
    obs.subscribe({ next: () => this.router.navigate(['/customers']),
      error: e => this.error = e?.error?.error || 'Validation error' });
  }
  cancel() { this.router.navigate(['/customers']); }
}
