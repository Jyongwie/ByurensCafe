import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

type OrderTab = "current" | "history";

@Component({
  selector: 'app-order',
  imports: [CommonModule],
  templateUrl: './order.html',
  styleUrl: './order.css',
})
export class Order {
  activeTab: OrderTab = "current";

  setTab(tab: OrderTab) {
    this.activeTab = tab;
  }
}
