import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { user } from '../../../models/user';
import { Observable } from 'rxjs';
import { AsyncPipe } from "@angular/common";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, AsyncPipe],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  userAuth$!: Observable<user | null>;
  authenticated: boolean = false;

  constructor(private auth: AuthService) {
    this.userAuth$ = this.auth.currentUser$;
    //this.userAuth$.subscribe(user => console.log('[Header] Got user:', user));
  }

  logout() {
    this.auth.logout();
  }

  isLogged() {
    return this.auth.userIsLogged();
  }

}
