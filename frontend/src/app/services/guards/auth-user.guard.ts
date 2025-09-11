import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class authUserGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router) { }

  canActivate() {
    if (this.authService.userIsLogged) {
      return true;
    }
    this.router.navigate(['/user/login']);
    return false;
  }
}