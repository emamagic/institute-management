import {Injectable} from '@angular/core';
import {HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse} from '@angular/common/http';
import {BehaviorSubject, finalize, Observable, take, throwError} from 'rxjs';
import {catchError, filter, switchMap} from 'rxjs/operators';
import {JwtService} from '../my-service/jwt.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  constructor(
    private jwtSvc: JwtService
  ) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const accessToken = this.jwtSvc.getAccessToken();

    if (accessToken != null) {
      request = this.addToken(request, accessToken);
    }

    return next.handle(request).pipe(
      catchError((error) => {
        console.log("refresh a")
        if (error instanceof HttpErrorResponse && (error.status === 401)) {
          console.log("refresh b")
          if (request.url.includes('auth/refresh')) {
            console.log("refresh c")
            this.jwtSvc.logout();
            return throwError(() => error);
          }
          console.log("refresh d")
          return this.handle401Error(request, next);
        }
        return throwError(() => error);
      })
    );
  }

  private addToken(request: HttpRequest<any>, token: string): HttpRequest<any> {
    return request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  private handle401Error(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);

      return this.jwtSvc.refresh().pipe(
        switchMap(resp => {
          this.jwtSvc.storeJwtToken(resp);
          this.refreshTokenSubject.next(resp.accessToken);
          const clonedRequest = request.clone({
            setHeaders: {Authorization: `Bearer ${resp.accessToken}`}
          });
          return next.handle(clonedRequest);
        }),
        catchError(err => {
          this.jwtSvc.logout();
          return throwError(() => err);
        }),
        finalize(() => {
          this.isRefreshing = false;
        })
      );
    } else {
      return this.refreshTokenSubject.pipe(
        filter(token => token != null),
        take(1),
        switchMap((newAccessToken) => {
          const clonedRequest = request.clone({
            setHeaders: {Authorization: `Bearer ${newAccessToken}`}
          });
          return next.handle(clonedRequest);
        })
      );
    }
  }
}
