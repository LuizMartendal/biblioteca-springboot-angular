import { Component, OnInit } from '@angular/core';
import { delay, finalize } from 'rxjs';
import { BookService } from 'src/app/core/entities/book/book.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
})
export class ListComponent  implements OnInit {

  public books: any;

  public isLoading = false;
  public page = 0;
  public size = 10;

  constructor(
    private bookService: BookService
  ) { }

  ngOnInit() {
    this.list();
  }

  list(page?: number, size?: number, filter?: string) {
    this.isLoading = true;
    this.bookService.retrieveAll({
      page: page ? page : this.page,
      size: size ? size : this.size,
      filtro: filter ? filter : '',
    }).pipe(delay(5000), finalize(() => {
      this.isLoading = false;
    })).subscribe((response: any) => {
      this.books = response;
    });
  }

}
