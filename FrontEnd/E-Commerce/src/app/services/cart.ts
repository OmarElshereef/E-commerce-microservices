import { computed, Injectable, signal } from '@angular/core';
import { Product } from '../models/product.model';
import { CartItem } from '../models/cartItem.model';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  cart = signal<CartItem[]>([]);

  // Computed signals for derived data
  totalItems = computed(() =>
    this.cart().reduce((total, item) => total + item.quantity, 0)
  );

  totalPrice = computed(() =>
    this.cart().reduce((total, item) => total + (item.price * item.quantity), 0)
  );

  isEmpty = computed(() => this.cart().length === 0);

  addToCart(product: Product, quantity: number = 1) {
    const currentCart = this.cart();
    const existingItemIndex = currentCart.findIndex(item => item.productId === product.id);

    if (existingItemIndex !== -1) {
      // Item already exists, update quantity
      const updatedCart = [...currentCart];
      updatedCart[existingItemIndex] = {
        ...updatedCart[existingItemIndex],
        quantity: updatedCart[existingItemIndex].quantity + quantity
      };
      this.cart.set(updatedCart);
    } else {
      // New item, add to cart
      const newCartItem: CartItem = {
        id: this.generateCartItemId(),
        productId: product.id,
        productName: product.name,
        productImage: product.imageUrl,
        price: product.price,
        quantity: quantity
      };
      this.cart.set([...currentCart, newCartItem]);
    }

    // TODO: Make API request to add/update item in backend
    console.log(`Added ${quantity} x ${product.name} to cart`);
  }

  updateQuantity(cartItemId: number, newQuantity: number) {
    if (newQuantity <= 0) {
      this.removeItem(cartItemId);
      return;
    }

    const currentCart = this.cart();
    const updatedCart = currentCart.map(item =>
      item.id === cartItemId
        ? { ...item, quantity: newQuantity }
        : item
    );
    this.cart.set(updatedCart);

    // TODO: Make API request to update quantity in backend
    console.log(`Updated cart item ${cartItemId} quantity to ${newQuantity}`);
  }

  increaseQuantity(cartItemId: number) {
    const currentCart = this.cart();
    const item = currentCart.find(item => item.id === cartItemId);
    if (item) {
      this.updateQuantity(cartItemId, item.quantity + 1);
    }
  }

  decreaseQuantity(cartItemId: number) {
    const currentCart = this.cart();
    const item = currentCart.find(item => item.id === cartItemId);
    if (item) {
      this.updateQuantity(cartItemId, item.quantity - 1);
    }
  }

  removeItem(cartItemId: number) {
    const currentCart = this.cart();
    const updatedCart = currentCart.filter(item => item.id !== cartItemId);
    this.cart.set(updatedCart);

    // TODO: Make API request to remove item from backend
    console.log(`Removed cart item ${cartItemId}`);
  }

  clearCart() {
    this.cart.set([]);

    // TODO: Make API request to clear cart in backend
    console.log('Cart cleared');
  }

  getCartItem(cartItemId: number): CartItem | undefined {
    return this.cart().find(item => item.id === cartItemId);
  }

  getCartItemByProductId(productId: number): CartItem | undefined {
    return this.cart().find(item => item.productId === productId);
  }

  isProductInCart(productId: number): boolean {
    return this.cart().some(item => item.productId === productId);
  }

  getProductQuantityInCart(productId: number): number {
    const item = this.getCartItemByProductId(productId);
    return item ? item.quantity : 0;
  }

  // Load cart from backend (call this on app initialization)
  async loadCart(userId: number) {
    // TODO: Make API request to get user's cart items
    // const cartItems = await this.http.get<CartItem[]>(`/api/cart/user/${userId}`).toPromise();
    // this.cart.set(cartItems);

    console.log(`Loading cart for user ${userId} - TODO: Implement API call`);
  }

  // Helper method to generate unique cart item IDs (temporary, backend will provide real IDs)
  private generateCartItemId(): number {
    return Math.floor(Math.random() * 1000000) + Date.now();
  }

  // Utility methods for checkout process
  validateCart(): { isValid: boolean; errors: string[] } {
    const errors: string[] = [];

    if (this.isEmpty()) {
      errors.push('Cart is empty');
    }

    // Check for items with zero or negative quantities
    const invalidItems = this.cart().filter(item => item.quantity <= 0);
    if (invalidItems.length > 0) {
      errors.push('Cart contains items with invalid quantities');
    }

    return {
      isValid: errors.length === 0,
      errors
    };
  }

  // Get cart summary for display
  getCartSummary() {
    return {
      items: this.cart(),
      totalItems: this.totalItems(),
      totalPrice: this.totalPrice(),
      isEmpty: this.isEmpty()
    };
  }
}
