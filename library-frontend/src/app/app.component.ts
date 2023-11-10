import { Component, OnInit } from '@angular/core';
import { AppStorageService } from './core/app-storage/app-storage.service';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
})
export class AppComponent implements OnInit {

  constructor(
    private appStorage: AppStorageService
  ) {}

  ngOnInit(): void {
    
  }

  hasSession() {
    return this.appStorage.get(AppStorageService.KEY_STORAGE.token);
  }

  logout() {

  }
}
