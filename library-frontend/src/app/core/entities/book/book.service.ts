import { Injectable } from '@angular/core';
import { EntityService } from '../EntityService';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Book } from 'src/app/interfaces/book/Book';

@Injectable({
  providedIn: 'root'
})
export class BookService extends EntityService<Book> {

  constructor(
    private httpClient: HttpClient
  ) {
    super(httpClient, `${environment.URL}/book`)
   }
}
