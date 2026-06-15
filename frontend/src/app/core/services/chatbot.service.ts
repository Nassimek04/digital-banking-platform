import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ChatbotService {
  constructor(private http: HttpClient) {}
  ask(message: string): Observable<{ response: string }> {
    return this.http.post<{ response: string }>(`${environment.apiUrl}/chatbot/ask`, { message });
  }
}
