import {Injectable} from '@angular/core';
import {
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HttpEvent,
  HttpResponse
} from '@angular/common/http';
import {Observable, map} from 'rxjs';

// or use this lib -> npm install camelcase-keys
// return next.handle(req).pipe(
//   map(event => {
//     if (event instanceof HttpResponse && event.body && typeof event.body === 'object') {
//       const modifiedBody = keysToCamelCase(event.body);
//       return event.clone({ body: modifiedBody });
//     }
//     return event;
//   })
// );


@Injectable()
export class SnakeCamelInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (req.body && typeof req.body === 'object') {
      const modifiedBody = this.keysToSnakeCase(req.body);
      req = req.clone({ body: modifiedBody });
    }

    return next.handle(req).pipe(
      map(event => {
        if (event instanceof HttpResponse && event.body && typeof event.body === 'object') {
          const modifiedBody = this.keysToCamelCase(event.body);
          return event.clone({ body: modifiedBody });
        }
        return event;
      })
    );
  }

  private keysToCamelCase(obj: any): any {
    if (Array.isArray(obj)) {
      return obj.map(item => this.keysToCamelCase(item));
    } else if (obj !== null && typeof obj === 'object') {
      return Object.keys(obj).reduce((acc, key) => {
        const camelKey = key.replace(/_([a-z])/g, (_, letter) => letter.toUpperCase());
        acc[camelKey] = this.keysToCamelCase(obj[key]);
        return acc;
      }, {} as any);
    }
    return obj;
  }

  private keysToSnakeCase(obj: any): any {
    if (Array.isArray(obj)) {
      return obj.map(item => this.keysToSnakeCase(item));
    } else if (obj !== null && typeof obj === 'object') {
      return Object.keys(obj).reduce((acc, key) => {
        const snakeKey = key.replace(/[A-Z]/g, letter => `_${letter.toLowerCase()}`);
        acc[snakeKey] = this.keysToSnakeCase(obj[key]);
        return acc;
      }, {} as any);
    }
    return obj;
  }
}
