import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AppStorageService } from 'src/app/core/app-storage/app-storage.service';
import { Token } from 'src/app/interfaces/util/Token';

@Injectable({
  providedIn: 'root'
})
export class AuthRedirectGuard implements CanActivate {

  constructor(
    private appStorage: AppStorageService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const token: Token = this.appStorage.get(AppStorageService.KEY_STORAGE.token);
      if (token) {
        this.router.navigate(['/book']);
        return false;
      }
      return true;
  }
  
}
