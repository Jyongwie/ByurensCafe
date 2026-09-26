import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

type OrderTab = "current" | "history";

interface MenuItem {id: number; name: string; price: number; category: string;}
interface CartItem {id: number; name: string; price: number; quantity: number;}

@Component({
  selector: 'app-order',
  imports: [CommonModule],
  templateUrl: './order.html',
  styleUrl: './order.css',
})
export class Order {
  activeTab: OrderTab = "current";

  // dummy data
  categories = ["All", "Espresso", "Non-Coffee", "Pastry"];
  activeCategory = "All";

  menuItems: MenuItem[] = [
    { id: 1, name: 'Iced Caramel Macchiato', price: 35000, category: 'Espresso' },
    { id: 2, name: 'Cafe Latte', price: 30000, category: 'Espresso' },
    { id: 3, name: 'Matcha Latte', price: 32000, category: 'Non-Coffee' },
    { id: 4, name: 'Butter Croissant', price: 25000, category: 'Pastry' },
    { id: 5, name: 'Almond Croissant', price: 30000, category: 'Pastry' },
    { id: 6, name: 'Americano', price: 25000, category: 'Espresso' },
    { id: 7, name: 'Americano', price: 25000, category: 'Espresso' },
    { id: 8, name: 'Americano', price: 25000, category: 'Espresso' },
    { id: 9, name: 'Americano', price: 25000, category: 'Espresso' },
    { id: 10, name: 'Americano', price: 25000, category: 'Espresso' },
    { id: 11, name: 'Americano', price: 25000, category: 'Espresso' },
    { id: 12, name: 'Americano', price: 25000, category: 'Espresso' },
    { id: 13, name: 'Americano', price: 25000, category: 'Espresso' },
    { id: 14, name: 'Americano', price: 25000, category: 'Espresso' },
    { id: 15, name: 'Americano', price: 25000, category: 'Espresso' },
  ];

  cart: CartItem[] = [
    { id: 1, name: 'Iced Caramel Macchiato', price: 35000, quantity: 2 },
    { id: 4, name: 'Butter Croissant', price: 25000, quantity: 1 }
  ]

  get cartTotal(): number {
    return this.cart.reduce((total, item) => total + (item.price * item.quantity), 0);
  }

  setTab(tab: OrderTab) {
    this.activeTab = tab;
  }

  setCategory(cat: string) {
    this.activeCategory = cat;
  }

  getQuantity(itemId: number): number {
    const item = this.cart.find(c => c.id === itemId);
    return item? item.quantity : 0;
  }

  addToCart(menuItem: MenuItem, event?: Event) {
    if (event) {
      event.stopPropagation();
    }
    const existing = this.cart.find(c => c.id === menuItem.id);
    if (existing) {
      existing.quantity++;
    } else {
      this.cart.push({...menuItem, quantity: 1})
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
    this.cart = this.cart.filter(c => c.id !== itemId); 
  }
}
