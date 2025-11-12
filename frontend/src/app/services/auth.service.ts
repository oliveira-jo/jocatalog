import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { catchError, map, switchMap, tap } from 'rxjs/operators';
import { LoginResponse } from '../models/login-response.type';
import { user } from '../models/user';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = `${environment.baseUrl}/api/v1/oauth2/token`;
  private apiUrlMe = `${environment.baseUrl}/api/v1/auth/me`;
  private token: string | null = null;
  //user!: user | null;

  private currentUserSubject = new BehaviorSubject<user | null>(null);
  currentUser$: Observable<user | null> = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) { }

  login(username: string, password: string): Observable<void> {

    const body = `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}&grant_type=password`;
    const headers = new HttpHeaders({
      "Authorization": "Basic bXljbGllbnRpZDpteWNsaWVudHNlY3JldA==",
      "Content-Type": "application/x-www-form-urlencoded"
    });

    return this.http.post<LoginResponse>(this.apiUrl, body, { headers: headers }).pipe(
      tap(res => {
        if (res.access_token) {
          localStorage.setItem('access_token', btoa(JSON.stringify(res.access_token)));
        }
      }),
      switchMap(() => this.loadCurrentUser()), // load user after token
      tap(user => this.currentUserSubject.next(user)), // emit user to all observers
      map(() => void 0) // convert to void observable
    );

  }

  loadCurrentUser(): Observable<user> {
    const bear = `Bearer ${this.getToken}`;
    const headers = new HttpHeaders({
      "Authorization": `${bear}`
    });
    return this.http.get<user>(this.apiUrlMe, { headers: headers });
  }

  setToken(token: string): void {
    this.token = token;
    localStorage.setItem('access_token', btoa(JSON.stringify(token)));
  }

  logout(): void {
    this.token = null;
    //this.user = null;

    localStorage.removeItem('access_token');
    this.currentUserSubject.next(null); // emit null to observers
    this.router.navigate(['/products/search']);
  }

  get getToken(): string | null {
    return localStorage.getItem('access_token')
      ? JSON.parse(atob(localStorage.getItem('access_token')!))
      : null;
  }

  get getUserAuthenticated(): user | null {
    return this.currentUserSubject.value;
    //return this.userIsLogged() ? this.user : null;
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
    console.error('API Error:', msgErro);
    return throwError(msgErro);
  }
}
