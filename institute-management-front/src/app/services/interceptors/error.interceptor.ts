import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { AlertService } from '../my-service/alert.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private alertService: AlertService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        this.handleError(error);
        return throwError(() => error);
      })
    );
  }

  private handleError(error: HttpErrorResponse): void {
    if (error.error instanceof ErrorEvent) {
      this.alertService.showError('A client-side error occurred: ' + error.error.message);
    } else {
      const apiError = error.error as { error?: string; validation_errors?: Set<string> };

      if (apiError?.validation_errors) {
        const validationMessages = Array.from(apiError.validation_errors).join('\n');
        this.alertService.showError(`${validationMessages}`);
      } else if (apiError?.error) {
        this.alertService.showError(apiError.error);
      } else {
        this.alertService.showError('An unexpected error occurred');
      }
    }
  }
}
