import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { product } from '../models/product';
import { AuthService } from './auth.service';
import { category } from '../models/category';


@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private urlApi = `${environment.baseUrl}/api/v1/categories`;
  private jsonHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient, private auth: AuthService) {

  }

  getCategories(): Observable<category[]> {

    var response = this.http.get<category[]>(this.urlApi, { headers: this.jsonHeaders })
      .pipe(
        catchError(this.handleError)
      );
    return response;
  }

  getCategory(id: string): Observable<category> {
    if (id === '') {
      return of(this.initCategory());
    }
    const urlId = `${this.urlApi}/${id}`;
    return this.http.get<category>(urlId, { headers: this.jsonHeaders })
      .pipe(
        catchError(this.handleError)
      );
  }

  create(category: category) {

    const bear = `Bearer ${this.auth.getToken}`;
    const headers = new HttpHeaders({
      "Authorization": `${bear}`
    });

    return this.http.post<product>(this.urlApi, category, { headers: headers })
      .pipe(
        catchError(this.handleError)
      );

  }

  update(category: category) {

    const bear = `Bearer ${this.auth.getToken}`;
    const headers = new HttpHeaders({
      "Authorization": `${bear}`
    });

    const urlId = `${this.urlApi}/${category.id}`;
    return this.http.put<product>(urlId, category, { headers: headers })
      .pipe(
        catchError(this.handleError)
      );

  }

  delete(id: string) {
    const bear = `Bearer ${this.auth.getToken}`;
    const headers = new HttpHeaders({
      "Authorization": `${bear}`
    });

    const urlId = `${this.urlApi}/${id}`;
    return this.http.delete<category>(urlId, { headers: headers })
      .pipe(
        catchError(this.handleError)
      );
  }

  private handleError(e: { error: { message: any; }; status: any; body: { error: any; }; }) {

    let msgErro: string;
    if (e.error instanceof ErrorEvent) {
      console.log(`* Error * : ${e.error.message}`);
      msgErro = `* Error * : ${e.error.message}`;
    } else {
      msgErro = `* Error API. * StatusCode* : ${e.status}, Desc.: ${e.body.error}`;
      console.log(`* Error API. * StatusCode* : ${e.status}, Desc.: ${e.body.error}`);
    }
    return throwError(msgErro);
  }

  private initCategory(): category {
    return {
      id: null,
      name: null
    }
  }

}
