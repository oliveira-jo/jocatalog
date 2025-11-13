import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from "./components/shared/header/header.component";
import { BaseUiComponent } from "./components/shared/base-ui/base-ui.component";
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',

  imports: [RouterOutlet, HeaderComponent, BaseUiComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'JoCatalog - Home';

  constructor(private auth: AuthService) { }

  ngOnInit() {
    if (this.auth.userIsLogged() && !this.auth.getUserAuthenticated) {
      this.auth.loadCurrentUser().subscribe({
        next: user => this.auth['currentUserSubject'].next(user),
        error: () => this.auth.logout()
      });
    }
  }

}