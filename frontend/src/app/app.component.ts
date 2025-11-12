import { Component, OnDestroy, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from "./components/shared/header/header.component";
import { BaseUiComponent } from "./components/shared/base-ui/base-ui.component";
import { AuthService } from './services/auth.service';
import { Subscription } from 'rxjs';

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

    const token = this.auth.getToken;
    if (token) {
      this.auth.loadCurrentUser().subscribe({
        next: user => this.auth['currentUserSubject'].next(user),
        error: () => this.auth.logout()
      });
    }
  }
}