import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { OrderService } from '../../../core/services/order.service';
import { FormsModule } from '@angular/forms';
import { VenueService } from '../../../core/services/venue.service';
import { TableCafe } from '../../../models/venue.model';
import { OrderRequest } from '../../../models/order.model';
import { CategoryResponse } from '../../../models/product.model';
import { ProductService } from '../../../core/services/product.service';

type OrderTab = "current" | "history";
type CartPhase = "INIT" | "ACTIVE";
type OrderType = "DINE_IN" | "TAKEAWAY";

interface MenuItem {id: string; variantId: string; name: string; price: number; categoryLabel: string;}
interface CartItem extends MenuItem {quantity: number; note?: string; addOns: string[];}

@Component({
  selector: 'app-order',
  imports: [CommonModule, FormsModule],
  templateUrl: './order.html',
  styleUrl: './order.css',
})
export class Order implements OnInit {
  private orderService = inject(OrderService);
  private venueService = inject(VenueService);
  private productService = inject(ProductService);

  activeTab: OrderTab = "current";
  cartPhase: CartPhase = "INIT";
  categories: CategoryResponse[] = []
  activeCategory = "All";

  menuItems: MenuItem[] = [];
  filteredMenuItems: MenuItem[] = []

  selectedOrderType: OrderType | null = null;
  tables: TableCafe[] = [];
  selectedTable: TableCafe | null = null;
  customerName: string = "";

  cart: CartItem[] = [];
  isSubmitting = false;

  ngOnInit(): void {
    this.venueService.getTables().subscribe(data => this.tables = data);

    this.productService.getCategories().subscribe(data => this.categories = data);

    this.productService.getProducts().subscribe(products => {
      this.menuItems = [];
      products.forEach(p => {
        p.variants.forEach(v => {
          this.menuItems.push({
            id: p.id,
            variantId: v.id,
            name: p.variants.length > 1 ? `${p.name} (${v.size})` : p.name,
            price: v.price,
            categoryLabel: p.category.label
          })
        })
      })
      this.filterMenu();
    })
  }

  setCategory(cat: string) {
    this.activeCategory = cat;
    this.filterMenu();
  }

  filterMenu() {
    if (this.activeCategory === "All") {
      this.filteredMenuItems = [...this.menuItems];
    } else {
      this.filteredMenuItems = this.menuItems.filter(item => item.categoryLabel === this.activeCategory);
    }
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
  getQuantity(variantId: string): number {
    return this.cart.find(c => c.variantId === variantId)?.quantity || 0;
  }

  addToCart(menuItem: MenuItem, event?: Event) {
    if (event) {
      event.stopPropagation();
    }
    if (this.cartPhase !== "ACTIVE") {
      return;
    }

    const existing = this.cart.find(c => c.variantId === menuItem.variantId);
    if (existing) {
      existing.quantity++;
    } else {
      this.cart.push({...menuItem, quantity: 1, addOns: []});
    }
  }

  removeFromCart(variantId: string, event?: Event) {
    if (event) {
      event.stopPropagation();
    }
    const existingIndex = this.cart.findIndex(c => c.variantId === variantId);
    if (existingIndex > -1) {
      if (this.cart[existingIndex].quantity > 1) {
        this.cart[existingIndex].quantity--;
      } else {
        this.cart.splice(existingIndex, 1);
      }
    }
  }

  removeAllOfItem(variantId: string) {
    this.cart = this.cart.filter(c => c.variantId !== variantId)
  }

  submitOrder() {
    if (!this.selectedOrderType || this.cart.length === 0) {
      return;
    }
    this.isSubmitting = true;

    const payload: OrderRequest = {
      orderType: this.selectedOrderType,
      tableId: this.selectedTable?.id || null,
      customerId: null,
      items: this.cart.map(item => ({
        variantId: item.variantId,
        quantity: item.quantity,
        note: item.note || "",
        addOnsId: item.addOns
      }))
    };

    this.orderService.submitOrder(payload).subscribe({
      next: (res) => {
        alert("Order sent to kitchen");
        this.cancelOrder();
        this.isSubmitting = false;
      },
      error: (err) => {
        console.error("Order failed: ", err);
        this.isSubmitting = false;
      }
    })
  }

  setTab(tab: OrderTab) {
    this.activeTab = tab;
  }
}
