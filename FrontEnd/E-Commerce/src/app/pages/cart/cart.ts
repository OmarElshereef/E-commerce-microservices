import { Component, inject, signal } from '@angular/core';
import { CartService } from '../../services/cart';
import { CartCard } from "./cart-card/cart-card";
import { CartItem } from '../../models/cartItem.model';
import { PrimaryButton } from "../../components/primary-button/primary-button";

@Component({
  selector: 'app-cart',
  imports: [CartCard, PrimaryButton],
  templateUrl: './cart.html',
  styleUrl: './cart.css'
})
export class Cart {
    cartService = inject(CartService);
    cartItems = this.cartService.cart;

}
