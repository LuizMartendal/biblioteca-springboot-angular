import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EntityService } from '../EntityService';
import { environment } from 'src/environments/environment';
import { User } from 'src/app/interfaces/user/User';

@Injectable({
  providedIn: 'root'
})
export class UserService extends EntityService<User> {

  constructor(
    private httpClient: HttpClient
  ) {
    super(httpClient, `${environment.URL}/person`)
   }

   login(credentials: Credential) {
    return this.http.post(`${environment.URL}/login`, credentials);
   }
}
