import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-primary-button',
  imports: [],
  templateUrl: './primary-button.html',
  styleUrl: './primary-button.css'
})
export class PrimaryButton {
    label = input<string>();
    btnClicked = output();
}
