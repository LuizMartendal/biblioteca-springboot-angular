import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable, catchError, map, throwError } from 'rxjs';
import { AppStorageService } from '../app-storage/app-storage.service';
import { Router } from '@angular/router';
import { AppNotifyService } from '../app-notify/app-notify.service';
import { Token } from 'src/app/interfaces/util/Token';

@Injectable()
export class InterceptorInterceptor implements HttpInterceptor {

  private toastDisplayed = false;

  constructor(
    private appStorage: AppStorageService,
    private router: Router,
    private appNotify: AppNotifyService
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token: Token = this.appStorage.get(AppStorageService.KEY_STORAGE.token);
    if (token) {
      request = request.clone({headers: request.headers.set('Authorization', token.token || '')});
    }
    return next.handle(request)
    .pipe(catchError((error: any) => {
      if (error.status === 403) {
        localStorage.clear();
        this.router.navigate(['/']);
        
        if (!this.toastDisplayed) {
          this.appNotify.errorMessage(error.error.message);
          this.toastDisplayed = true;
        }
      } else if (error.status === 0) {
        localStorage.clear();
        this.router.navigate(['/']);

        if (!this.toastDisplayed) {
          this.appNotify.errorMessage("Sem conexão. Tente novamente mais tarde...");
          this.toastDisplayed = true;
        }
      }
      return throwError(error);
    }),
    map((event: any) => event));
  }
}
