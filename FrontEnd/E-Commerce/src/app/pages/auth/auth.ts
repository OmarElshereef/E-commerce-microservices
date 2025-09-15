import { Component, signal } from '@angular/core';
import { PrimaryButton } from "../../components/primary-button/primary-button";
import { SecondoryButton } from '../../components/secondory-button/secondory-button';
import { AuthService } from '../../services/auth-service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-auth',
  imports: [PrimaryButton, SecondoryButton],
  templateUrl: './auth.html',
  styleUrl: './auth.css'
})
export class Auth {

    mode = signal<'signin' | 'signup'>('signin');

    email = signal('');
    password = signal('');
    name = signal('');

    constructor(private authService: AuthService, private router: Router) {}

    setMode(newMode: 'signin' | 'signup') {
        this.mode.set(newMode);
    }

    onSignIn() {
        this.authService.login(this.email(), this.password()).subscribe({
        next: () => this.router.navigate(['/cart']),
        error: (err) => {
            console.error('❌ Login failed:', err);
        }
        });
    }

    onSignUp() {
        this.authService.register({
        name: this.name(),
        email: this.email(),
        password: this.password()
        }).subscribe({
        next: () => this.router.navigate(['/']),
        error: (err) => {
            console.error('❌ Register failed:', err);
        }
        });
    }
}
