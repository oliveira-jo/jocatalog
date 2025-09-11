import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { catchError, tap } from 'rxjs/operators';
import { LoginResponse } from '../models/login-response.type';



@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient, private router: Router) { }

  login(username: string, password: string): Observable<LoginResponse> {
    const urlLogin = `${environment.baseUrl}/api/v1/auth/login`;
    return this.http.post<LoginResponse>(urlLogin, { username, password })
      .pipe(
        tap((response) => {
          if (response.access_token === '') return;
          localStorage.setItem('token', btoa(JSON.stringify(response.access_token)));
          // localStorage.setItem('username', btoa(JSON.stringify(response.username)));
          // localStorage.setItem('id', btoa(JSON.stringify(response.id)));
        }),
        (catchError(this.handleError))
      );
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/']);
  }

  get getLoggedUser(): any {
    return localStorage.getItem('username')
      ? JSON.parse(atob(localStorage.getItem('username')!))
      : null;
  }

  get getIdLoggedUser(): string | null | undefined {
    return localStorage.getItem('id')
      ? (JSON.parse(atob(localStorage.getItem('id')!)))
      : null;
  }

  get getUserToken(): string | null {
    return localStorage.getItem('token')
      ? JSON.parse(atob(localStorage.getItem('token')!))
      : null;
  }

  get userIsLogged(): boolean {
    return localStorage.getItem('token') ? true : false;
  }

  private handleError(e: { error: { message: any; }; status: any; body: { error: any; }; }) {
    let msgErro: string;
    if (e.error instanceof ErrorEvent) {
      msgErro = `* Error * : ${e.error.message}`;
    } else {
      msgErro = `* Error API. * StatusCode* : ${e.status}, Desc.: ${e.body.error}`;
    }
    return throwError(msgErro);
  }

}
