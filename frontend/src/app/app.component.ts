import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from "./components/shared/header/header.component";
import { BaseUiComponent } from "./components/shared/base-ui/base-ui.component";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, HeaderComponent, BaseUiComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'JoCatalog - Home';
}
