export interface OrderItemRequest {
    variantId: string;
    quantity: number;
    note?: string;
    addOnsId: string[];
}

export interface OrderRequest {
    customerId?: string | null;
    tableId?: string | null;
    orderType: "DINE_IN" | "TAKEAWAY";
    items: OrderItemRequest[];
}