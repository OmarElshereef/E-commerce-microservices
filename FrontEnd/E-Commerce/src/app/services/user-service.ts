import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
   private userSubject = new BehaviorSubject<User | null>(null);
    user$ = this.userSubject.asObservable();

    setUser(user: User | null) {
        this.userSubject.next(user);
    }

    getUser(): User | null {
        return this.userSubject.value;
    }
}
