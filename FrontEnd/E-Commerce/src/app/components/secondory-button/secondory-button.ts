import { Component, input, output } from '@angular/core';

@Component({
  selector: 'app-secondory-button',
  imports: [],
  templateUrl: './secondory-button.html',
  styleUrl: './secondory-button.css'
})
export class SecondoryButton {
    label = input<string>();
    btnClicked = output();
}
