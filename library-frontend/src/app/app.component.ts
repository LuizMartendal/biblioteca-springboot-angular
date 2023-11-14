import { Component, OnInit } from '@angular/core';
import { AppStorageService } from './core/app-storage/app-storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
})
export class AppComponent implements OnInit {

  public pages: any = [];

  public username: string = '';

  constructor(
    private appStorage: AppStorageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const token = this.appStorage.get(AppStorageService.KEY_STORAGE.token);
    if (token) {
      this.username = token.user;

      this.pages = [
        { name: 'Books', icon: 'library', link: 'book' },
        { name: 'New book', icon: 'add', link: 'book/create' },
        { name: 'My user', icon: 'person', link: 'user/my-user' },
      ];
    }
  }

  hasSession() {
    return this.appStorage.get(AppStorageService.KEY_STORAGE.token);
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
