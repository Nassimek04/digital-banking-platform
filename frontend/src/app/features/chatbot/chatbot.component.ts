import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatbotService } from '../../core/services/chatbot.service';

interface Msg { from: 'user' | 'bot'; text: string; }

@Component({
  selector: 'app-chatbot',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <button class="fab" (click)="open=!open">{{ open ? '✕' : '💬' }}</button>
  <div class="panel" *ngIf="open">
    <div class="head">Banking Assistant</div>
    <div class="body">
      <div *ngFor="let m of messages" class="msg" [class.user]="m.from==='user'">{{m.text}}</div>
      <div class="msg bot" *ngIf="loading">…</div>
    </div>
    <div class="foot">
      <input [(ngModel)]="input" (keyup.enter)="send()" placeholder="Ask about accounts…"/>
      <button (click)="send()">Send</button>
    </div>
  </div>`,
  styles: [`
    .fab{position:fixed;bottom:24px;right:24px;width:56px;height:56px;border-radius:50%;border:none;
      background:#2563eb;color:#fff;font-size:22px;cursor:pointer;box-shadow:0 4px 12px rgba(0,0,0,.2)}
    .panel{position:fixed;bottom:90px;right:24px;width:330px;height:440px;background:#fff;border-radius:12px;
      display:flex;flex-direction:column;box-shadow:0 8px 30px rgba(0,0,0,.2);overflow:hidden}
    .head{background:#2563eb;color:#fff;padding:12px;font-weight:600}
    .body{flex:1;overflow-y:auto;padding:12px;display:flex;flex-direction:column;gap:8px}
    .msg{padding:8px 12px;border-radius:10px;max-width:80%;background:#e2e8f0;align-self:flex-start}
    .msg.user{background:#2563eb;color:#fff;align-self:flex-end}
    .foot{display:flex;border-top:1px solid #e2e8f0}
    .foot input{flex:1;border:none;padding:10px;outline:none}
    .foot button{border:none;background:#2563eb;color:#fff;padding:0 16px;cursor:pointer}
  `]
})
export class ChatbotComponent {
  open = false; input = ''; loading = false;
  messages: Msg[] = [{ from: 'bot', text: 'Hi! Ask me about customers, accounts or operations.' }];
  constructor(private svc: ChatbotService) {}
  send() {
    const q = this.input.trim(); if (!q) return;
    this.messages.push({ from: 'user', text: q }); this.input = ''; this.loading = true;
    this.svc.ask(q).subscribe({
      next: r => { this.messages.push({ from: 'bot', text: r.response }); this.loading = false; },
      error: () => { this.messages.push({ from: 'bot', text: 'Sorry, the assistant is unavailable.' }); this.loading = false; }
    });
  }
}
