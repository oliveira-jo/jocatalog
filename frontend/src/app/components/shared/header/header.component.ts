import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { user } from '../../../models/user';

@Component({
  selector: 'app-header',
  imports: [RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  userAuth!: user | null;
  private auth: AuthService;
  authenticated: boolean = false;

  constructor(auth: AuthService) {
    this.auth = auth;
    this.userAuth = auth.getUserAuthenticated;
  }

  logout() {
    this.auth.logout();
  }

  isLogged() {
    return this.auth.userIsLogged();
  }

  isAdminLogged() {

    var logged = false;

    this.userAuth?.roles?.forEach(
      role => {
        if (role.authority === 'ROLE_ADMIN' || role.authority === 'ROLE_OPERATOR') {
          console.log(role.authority)
          logged = true

        } else {
          logged = false;
        }
      }
    );

    return logged;

  }

}
