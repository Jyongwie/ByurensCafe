import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { OrderService, TableCafe } from '../../../core/services/order.service';
import { FormsModule } from '@angular/forms';

type OrderTab = "current" | "history";
type CartPhase = "INIT" | "ACTIVE";
type OrderType = "DINE_IN" | "TAKEAWAY";

interface MenuItem {id: number; variantId: string; name: string; price: number; category: string;}
interface CartItem extends MenuItem {quantity: number; note?: string; addOns: string[];}

@Component({
  selector: 'app-order',
  imports: [CommonModule, FormsModule],
  templateUrl: './order.html',
  styleUrl: './order.css',
})
export class Order implements OnInit {
  private orderService = inject(OrderService)

  activeTab: OrderTab = "current";
  cartPhase: CartPhase = "INIT";
  categories = ["All", "Espresso", "Non-Coffee", "Pastry"];
  activeCategory = "All";

  selectedOrderType: OrderType | null = null;
  tables: TableCafe[] = [];
  selectedTable: TableCafe | null = null;
  customerName: string = "";

  cart: CartItem[] = [];
  isSubmitting = false;

// Dummy Menu Data
  menuItems: MenuItem[] = [
    { id: 1, variantId: '1111-2222-3333-4444', name: 'Iced Caramel Macchiato', price: 35000, category: 'Espresso' },
    { id: 2, variantId: '5555-6666-7777-8888', name: 'Cafe Latte', price: 30000, category: 'Espresso' },
    { id: 3, variantId: '9999-0000-1111-2222', name: 'Butter Croissant', price: 25000, category: 'Pastry' }
  ];

  ngOnInit(): void {
    this.orderService.getTables().subscribe({
      next: (data) => this.tables = data,
      error: () => {
        // fallback dummy data
        this.tables = [
          { id: 't1', tableIdentifier: '01', capacity: 2, status: 'AVAILABLE' },
          { id: 't2', tableIdentifier: '02', capacity: 4, status: 'OCCUPIED' },
          { id: 't3', tableIdentifier: '03', capacity: 2, status: 'AVAILABLE' },
        ];
      }
    });
  }

  selectOrderType(type: OrderType) {
    this.selectedOrderType = type;
    if (type === "TAKEAWAY") {
      this.selectedTable = null;
    }
  }

  selectTable(table: TableCafe) {
    if (table.status !== "AVAILABLE") {
      return;
    }
    this.selectedTable = table;
  }

  startOrder() {
    if (this.selectedOrderType === "DINE_IN" && !this.selectedTable) {
      return;
    }
    this.cartPhase = "ACTIVE";
  }

  cancelOrder() {
    this.cartPhase = "INIT";
    this.selectedOrderType = null;
    this.selectedTable = null;
    this.customerName = "";
    this.cart = [];
  }

  get cartTotal(): number {
    return this.cart.reduce((t, item) => t + (item.price * item.quantity), 0);
  }
  getQuantity(itemId: number): number {
    return this.cart.find(c => c.id === itemId)?.quantity || 0;
  }

  addToCart(menuItem: MenuItem, event?: Event) {
    if (event) {
      event.stopPropagation();
    }
    if (this.cartPhase !== "ACTIVE") {
      return;
    }

    const existing = this.cart.find(c => c.id === menuItem.id);
    if (existing) {
      existing.quantity++;
    } else {
      this.cart.push({...menuItem, quantity: 1, addOns: []});
    }
  }

  removeFromCart(itemId: number, event?: Event) {
    if (event) {
      event.stopPropagation();
    }
    const existingIndex = this.cart.findIndex(c => c.id === itemId);
    if (existingIndex > -1) {
      if (this.cart[existingIndex].quantity > 1) {
        this.cart[existingIndex].quantity--;
      } else {
        this.cart.splice(existingIndex, 1);
      }
    }
  }

  removeAllOfItem(itemId: number) {
    this.cart = this.cart.filter(c => c.id !== itemId)
  }
}
