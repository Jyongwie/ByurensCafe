export interface CategoryResponse {
    id: string;
    label: string;
}

export interface ProductVariantResponse {
    id: string;
    size: string;
    price: number;
}

export interface ProductResponse {
    id: string;
    name: string;
    category: CategoryResponse;
    variants: ProductVariantResponse[];
}
