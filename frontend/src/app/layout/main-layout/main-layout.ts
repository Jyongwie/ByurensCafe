import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, OnDestroy, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Sidebar } from '../sidebar/sidebar';

@Component({
  selector: 'app-main-layout',
  imports: [CommonModule, RouterOutlet, Sidebar],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.css',
})
export class MainLayout implements OnInit, OnDestroy {
  currentTime = signal<string>('');

  private timeInterval: ReturnType<typeof setInterval> | undefined;

  ngOnInit(): void {
    this.updateClock();
    this.timeInterval = setInterval(() => this.updateClock(), 60000);
  }

  ngOnDestroy(): void {
    if (this.timeInterval) {
      clearInterval(this.timeInterval);
    }
  }

  private updateClock(): void {
    const now = new Date();
    this.currentTime.set(now.toLocaleDateString([], {
      hour: '2-digit',
      minute: '2-digit'
    }));
  }
}
