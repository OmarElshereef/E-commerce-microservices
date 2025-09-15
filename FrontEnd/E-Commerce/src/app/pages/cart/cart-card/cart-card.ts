import { Component, inject, input } from '@angular/core';
import { CartItem } from '../../../models/cartItem.model';
import { CartService } from '../../../services/cart';
import { Button } from '../../../components/button/button';
@Component({
  selector: 'app-cart-card',
  imports: [Button],
  templateUrl: './cart-card.html',
  styleUrl: './cart-card.css'
})
export class CartCard {
    cartItem = input.required<CartItem>();
    cartService = inject(CartService);
}
