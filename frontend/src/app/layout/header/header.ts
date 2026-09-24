import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-header',
  imports: [CommonModule],
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header implements OnInit, OnDestroy {
  private http = inject(HttpClient);

  currentTime = signal<string>('');
  isSystemOnline = signal<boolean>(true);

  private timeInterval: ReturnType<typeof setInterval> | undefined;
  private healthSubscription: Subscription | undefined;

  ngOnInit(): void {
    this.updateClock();
    this.timeInterval = setInterval(() => this.updateClock(), 60000);

    this.checkSystemHealth();
    this.healthSubscription = interval(60000).subscribe(() => {
      this.checkSystemHealth();
    })
  }

  ngOnDestroy(): void {
    if (this.timeInterval) {
      clearInterval(this.timeInterval);
      this.healthSubscription?.unsubscribe();
    }
  }

  private updateClock(): void {
    const now = new Date();
    this.currentTime.set(now.toLocaleDateString([], {
      hour: '2-digit',
      minute: '2-digit'
    }));
  }

  private checkSystemHealth(): void {
    if (!navigator.onLine) {
      this.isSystemOnline.set(false);
      return;
    }

    this.http.get('http://localhost:8080/actuator/health', {responseType: 'text'})
      .subscribe({
        next: () => this.isSystemOnline.set(true),
        error: () => this.isSystemOnline.set(false)
      });
  }
}
