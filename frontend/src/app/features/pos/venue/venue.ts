import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { VenueService } from '../../../core/services/venue.service';
import { TableCafe, TableCafeRequest } from '../../../models/venue.model';

@Component({
  selector: 'app-venue',
  imports: [CommonModule, FormsModule],
  templateUrl: './venue.html',
  styleUrl: './venue.css',
})
export class Venue implements OnInit {
  private venueService = inject(VenueService);

  tables: TableCafe[] = [];

  isModalOpen = false;
  isEditing = false;
  isSubmitting = false;
  errorMessage = '';

  currentTableId: string | null = null;
  formData: TableCafeRequest = {
    tableIdentifier: "",
    capacity: 2,
    status: "AVAILABLE"
  }

  ngOnInit(): void {
    this.loadTables();
  }

  loadTables() {
    this.venueService.getTables().subscribe({
      next: (data) => this.tables = data,
      error: (err) => console.error("Failed to load tables", err)
    });
  }

  openAddModal() {
    this.isEditing = false;
    this.currentTableId = null;
    this.errorMessage = "";
    this.formData = {
      tableIdentifier: "",
      capacity: 2,
      status: "AVAILABLE"
    };
    this.isModalOpen = true;
  }

  openEditModal(table: TableCafe) {
    this.isEditing = true;
    this.currentTableId = table.id;
    this.errorMessage = "";
    this.formData = {
      tableIdentifier: table.tableIdentifier,
      capacity: table.capacity,
      status: table.status
    };
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
  }

  saveTable() {
    if (!this.formData.tableIdentifier || this.formData.capacity < 1) return;
    this.isSubmitting = true;
    this.errorMessage = "";

    const request$ = this.isEditing && this.currentTableId
      ? this.venueService.updateTable(this.currentTableId, this.formData)
      : this.venueService.createTable(this.formData)

    request$.subscribe({
      next: () => {
        this.loadTables();
        this.closeModal();
        this.isSubmitting = false;
      },
      error: (err) => {
        this.errorMessage = err.error?.message || "Failed to save table. Identifier might already exists."
        this.isSubmitting = false;
      }
    })
  }
}
