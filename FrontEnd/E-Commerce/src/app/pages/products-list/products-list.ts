import { Component, signal } from '@angular/core';
import { Product } from '../../models/product.model';
import { ProductCard } from './product-card/product-card';

@Component({
  selector: 'app-products-list',
  imports: [ProductCard],
  templateUrl: './products-list.html',
  styleUrl: './products-list.css'
})
export class ProductsList {
    products = signal<Product[]>([
        { id: 1, name: 'Product 1', description: 'Description for Product 1', price: 100, category: 'Category A', aviilableStock: 10, imageUrl: 'https://via.placeholder.com/150' },
        { id: 2, name: 'Product 2', description: 'Description for Product 2', price: 200, category: 'Category B', aviilableStock: 5, imageUrl: 'https://via.placeholder.com/150' },
        { id: 3, name: 'Product 3', description: 'Description for Product 3', price: 150, category: 'Category A', aviilableStock: 8, imageUrl: 'https://via.placeholder.com/150' },
        { id: 4, name: 'Product 4', description: 'Description for Product 4', price: 250, category: 'Category C', aviilableStock: 3, imageUrl: 'https://via.placeholder.com/150' },
    ]);
}
