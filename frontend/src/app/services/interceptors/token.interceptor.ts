
// import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse } from '@angular/common/http';
// import { throwError } from 'rxjs/internal/observable/throwError';
// import { environment } from '../../../environments/environment';
// import { Injectable } from '@angular/core';
// import { catchError } from 'rxjs/operators';
// import { Observable } from 'rxjs';

// import { AuthService } from '../auth.service';

// @Injectable({
//   providedIn: 'root'
// })
// export class TokenInterceptor implements HttpInterceptor {

//   constructor(private authService: AuthService) { }

//   intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

//     const token = this.authService.getUserToken;
//     const requestUrl: Array<any> = request.url.split('/');
//     const apiUrl: Array<any> = environment.baseUrl.split('/');

//     if (token && requestUrl[2] === apiUrl[2]) {

//       request = request.clone({
//         setHeaders: {
//           Authorization: `Bearer ${token}`,
//           token: `${token}`
//         }
//       });

//       return next.handle(request).pipe(
//         catchError(
//           error => {

//             if (error instanceof HttpErrorResponse && error.status === 401 || error.status === 403) {
//               this.authService.logout();
//             }
//             return throwError(error.message);
//           }
//         ));

//     } else {
//       return next.handle(request);
//     }
//   }
// }