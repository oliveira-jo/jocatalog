import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { catchError, tap } from 'rxjs/operators';
import { LoginResponse } from '../models/login-response.type';
import { user } from '../models/user';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = `${environment.baseUrl}/api/v1/oauth2/token`;
  private apiUrlMe = `${environment.baseUrl}/api/v1/auth/me`;
  private token: string | null = null;
  user!: user;

  constructor(private http: HttpClient, private router: Router) { }

  login(username: string, password: string): Observable<LoginResponse> {

    const body = `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}&grant_type=password`;

    const headers = new HttpHeaders({
      "Authorization": "Basic bXljbGllbnRpZDpteWNsaWVudHNlY3JldA==",
      "Content-Type": "application/x-www-form-urlencoded"
    });

    return this.http.post<LoginResponse>(this.apiUrl, body, { headers: headers }).pipe(
      tap((response) => {
        if (response.access_token === '') return;
        localStorage.setItem('access_token', btoa(JSON.stringify(response.access_token)));
      }),
      (catchError(this.handleError))
    );

  }

  me(): Observable<user> {

    const bear = `Bearer ${this.getToken}`;
    const headers = new HttpHeaders({
      "Authorization": `${bear}`
    });

    return this.http.post<user>(this.apiUrlMe, { headers: headers }).pipe(
      tap((response) => {
        this.user.id = response.id;
        this.user.first_name = response.first_name;
        this.user.last_name = response.last_name;
        this.user.email = response.email;
        this.user.roles = response.roles;
      }),
      (catchError(this.handleError))
    );

  }

  setToken(token: string): void {
    this.token = token;
    localStorage.setItem('access_token', btoa(JSON.stringify(token)));
  }

  logout(): void {
    this.token = null;
    localStorage.removeItem('access_token');
    localStorage.clear();
    this.router.navigate(['/']);
  }

  get getToken(): string | null {

    return localStorage.getItem('access_token')
      ? JSON.parse(atob(localStorage.getItem('access_token')!))
      : null;
  }

  get getUserAuthenticated(): user | null {

    return this.userIsLogged()
      ? this.user
      : null;
  }

  userIsLogged(): boolean {
    return localStorage.getItem('access_token') ? true : false;
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
