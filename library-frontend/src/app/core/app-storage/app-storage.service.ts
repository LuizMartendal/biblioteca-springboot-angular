import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AppStorageService {

  public static KEY = 'io.github.LuizMartendal';
  public static KEY_STORAGE = {
    token: `${AppStorageService.KEY}.token`
  }

  constructor() { }

  public get(key: any) {
    const value = localStorage.getItem(key);
    return value ? JSON.parse(value) : null;
  }

  public set(key: any, value: any) {
    localStorage.setItem(key, JSON.stringify(value));
  }
}
