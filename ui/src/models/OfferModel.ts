export interface Offer {
  id?: number
  title: string
  status?: any
  task?: any
  text: any
  active?: boolean
  validUntil?: string|null
  createdAt?: number
  taskId: number|null
  userId: number|null
}
export interface OfferDto {
  id?: number | null;
  userId: number | null;
  taskId: number | null;
  status?: OfferStatus;
  active?: boolean;
  text: string;
  title?: string | null;
  validUntil?: string | null;
}

export enum OfferStatus {
  SUBMITTED = 'SUBMITTED',
  REJECTED = 'REJECTED',
  ACCEPTED = 'ACCEPTED',
}
