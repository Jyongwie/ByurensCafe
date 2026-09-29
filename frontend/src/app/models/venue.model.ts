export interface TableCafe {
    id: string;
    tableIdentifier: string;
    capacity: number;
    status: "AVAILABLE" | "OCCUPIED" | "RESERVED";
}
