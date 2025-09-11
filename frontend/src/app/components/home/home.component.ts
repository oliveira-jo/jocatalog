import { Component } from '@angular/core';
import { HeaderComponent } from "../shared/header/header.component";
import { BaseUiComponent } from "../shared/base-ui/base-ui.component";
import { RouterOutlet } from "../../../../node_modules/@angular/router/router_module.d-Bx9ArA6K";
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
