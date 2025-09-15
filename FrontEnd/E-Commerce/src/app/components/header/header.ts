import { Component, signal, input, inject } from '@angular/core';
import { PrimaryButton } from "../primary-button/primary-button";
import { CartService } from '../../services/cart';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-header',
  imports: [PrimaryButton, RouterLink],
  templateUrl:'./header.html',
  styleUrl: './header.css'
})
export class Header {



    cartService = inject(CartService)

    showButtonCliced() {
        alert('Button clicked!');
    }
}
