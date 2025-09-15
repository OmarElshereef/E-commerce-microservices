import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {Header} from './components/header/header'
import { Auth } from "./pages/auth/auth";

@Component({
  selector: 'app-root',
  imports: [ RouterOutlet],
  template: `
  <router-outlet/>
  `,
  styleUrl: './app.css'
})
export class App {
}
