import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { DashboardResponse, DashboardService } from '../../../core/services/dashboard.service';
import { ChartConfiguration, ChartOptions } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, BaseChartDirective],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  authservice = inject(AuthService);
  private router = inject(Router);
  private dashboardService = inject(DashboardService);

  dashboardData?: DashboardResponse;

  mocha = '#4A3B32'
  mochaLight = 'rgba(74, 59, 50, 0.1)';
  cream = '#FDFBF7'

  baseOptions: ChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: { legend: { display: false } },
    scales: {
      x: { grid: { display: false }, border: { display: false } },
      y: { grid: { display: false }, border: { display: false }, ticks: { display: false } }
    }
  };

  lineChartData!: ChartConfiguration<'line'>['data'];
  lineChartOptions: ChartOptions<'line'> = { ...(this.baseOptions as any) };

  barChartData!: ChartConfiguration<'bar'>['data'];
  barChartOption: ChartOptions<'bar'> = {
    ...(this.baseOptions as any),
    indexAxis: 'y',
    scales: { x: { display: false }, y: { grid: { display: false }, border: { display: false } } }
  };

  pieChartData!: ChartConfiguration<'doughnut'>['data'];
  pieChartOptions: ChartOptions<'doughnut'> = {
    responsive: true,
    maintainAspectRatio: false,
    cutout: '75%',
    plugins: { legend: { position: 'bottom', labels: { usePointStyle: true, color: this.mocha } } }
  }

  ngOnInit(): void {
    this.dashboardService.getTodayDashboard().subscribe(data => {
      this.dashboardData = data;
      this.initCharts(data);
    })
  }

  private initCharts(data: DashboardResponse): void {
    this.lineChartData = {
      labels: data.hourlySales.map(s => `${s.hour}:00`),
      datasets: [{
        data: data.hourlySales.map(s => s.revenue),
        borderColor: this.mocha,
        backgroundColor: this.mochaLight,
        fill: true,
        tension: 0.4,
        pointRadius: 0
      }]
    };

    this.barChartData = {
      labels: data.topProducts.map(p => p.name),
      datasets: [{
        data: data.topProducts.map(p => p.quantity),
        backgroundColor: this.mocha,
        borderRadius: 4,
        barThickness: 24
      }]
    };
    
    this.pieChartData = {
      labels: data.paymentStats.map(p => p.method),
      datasets: [{
        data: data.paymentStats.map(p => p.count),
        backgroundColor: [this.mocha, '#6C584C', '#A98467', '#DDC3A5'],
        borderWidth: 0
      }]
    };
  }

  onLogout(): void {
    this.authservice.logout();
    this.router.navigate(['/login']);
  }
}
