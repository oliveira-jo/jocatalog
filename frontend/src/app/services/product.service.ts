import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { product } from '../models/product';
import { productsList } from '../models/products-list';
import { AuthService } from './auth.service';


@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private urlApi = `${environment.baseUrl}/api/v1/products`;
  private jsonHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient, private auth: AuthService) {

  }

  getProducts(): Observable<productsList> {

    var response = this.http.get<productsList>(this.urlApi, { headers: this.jsonHeaders })
      .pipe(
        catchError(this.handleError)
      );
    return response;
  }

  getProduct(id: string): Observable<product> {
    if (id === '') {
      return of(this.initProduct());
    }
    const urlId = `${this.urlApi}/${id}`;
    return this.http.get<product>(urlId, { headers: this.jsonHeaders })
      .pipe(
        catchError(this.handleError)
      );
  }

  create(product: product) {

    const bear = `Bearer ${this.auth.getToken}`;
    const headers = new HttpHeaders({
      "Authorization": `${bear}`
    });

    return this.http.post<product>(this.urlApi, product, { headers: headers })
      .pipe(
        catchError(this.handleError)
      );

  }

  update(product: product) {

    const bear = `Bearer ${this.auth.getToken}`;
    const headers = new HttpHeaders({
      "Authorization": `${bear}`
    });

    const urlId = `${this.urlApi}/${product.id}`;
    return this.http.put<product>(urlId, product, { headers: headers })
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
    return this.http.delete<product>(urlId, { headers: headers })
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

  private initProduct(): product {
    return {
      id: null,
      name: null,
      description: null,
      price: null,
      imgUrl: null,
      categories: null
    }
  }

}
