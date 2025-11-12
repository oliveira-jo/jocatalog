import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Product } from '../models/product';
import { productsList } from '../models/products-list';
import { AuthService } from './auth.service';
import { Page } from '../models/page';

@Injectable({ providedIn: 'root' })
export class ProductService {

  private urlApi = `${environment.baseUrl}/api/v1/products`;
  private jsonHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(
    private http: HttpClient,
    private auth: AuthService) {
  }

  searchProduct(name?: string, categoriId?: string): Observable<productsList> {

    let params = new HttpParams()
    if (name) {
      params = params.set('name', name);
    }
    if (categoriId) {
      params = params.set('categoryId', categoriId);
    }

    var response = this.http.get<productsList>(this.urlApi, { params })
      .pipe(
        catchError(this.handleError)
      );

    return response;
  }

  getProducts(page: number, size: number): Observable<Page<Product>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<Page<Product>>(this.urlApi, { params });
  }

  getProduct(id: string): Observable<Product> {
    if (id === '') {
      return of(this.initProduct());
    }
    const urlId = `${this.urlApi}/${id}`;
    return this.http.get<Product>(urlId, { headers: this.jsonHeaders })
      .pipe(
        catchError(this.handleError)
      );
  }

  create(product: Product) {
    const bear = `Bearer ${this.auth.getToken}`;
    const headers = new HttpHeaders({
      "Authorization": `${bear}`
    });

    return this.http.post<Product>(this.urlApi, product, { headers: headers })
      .pipe(
        catchError(this.handleError)
      );
  }

  update(product: Product) {
    const bear = `Bearer ${this.auth.getToken}`;
    const headers = new HttpHeaders({
      "Authorization": `${bear}`
    });

    const urlId = `${this.urlApi}/${product.id}`;
    return this.http.put<Product>(urlId, product, { headers: headers })
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
    return this.http.delete<Product>(urlId, { headers: headers })
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

  private initProduct(): Product {
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
