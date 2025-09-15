import { Component, inject, input } from '@angular/core';
import { Product } from '../../../models/product.model';
import { PrimaryButton } from "../../../components/primary-button/primary-button";
import { CartService } from '../../../services/cart';

@Component({
  selector: 'app-product-card',
  imports: [PrimaryButton],
  templateUrl: './product-card.html',
  styleUrl: './product-card.css'
})
export class ProductCard {
    product = input.required<Product>();
    cartService = inject(CartService);
}
