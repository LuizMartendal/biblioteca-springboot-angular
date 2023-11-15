import { Component, OnInit } from '@angular/core';
import { Platform } from '@ionic/angular';
import { delay, finalize } from 'rxjs';
import { BookService } from 'src/app/core/entities/book/book.service';
import { PlatformService } from 'src/app/core/platform/platform.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
})
export class ListComponent  implements OnInit {

  public books: any;

  public isLoading = false;
  public isMobile = false;

  public page: any = 0;
  public size: any = 10;

  constructor(
    private bookService: BookService,
    private platformService: PlatformService
  ) { }

  ngOnInit() {
    this.isMobile = this.platformService.isMobile();

    this.list();
  }

  list(page?: number, size?: number, filter?: string) {
    this.isLoading = true;

    if (this.isMobile) {
      this.size = undefined;
    }

    this.bookService.retrieveAll({
      page: page ? page : this.page,
      size: size ? size : this.size,
      filtro: filter ? filter : '',
    }).pipe(finalize(() => {
      this.isLoading = false;
    })).subscribe((response: any) => {
      this.books = response;
    });
  }

  onPage(event: any) {
    this.page = event.pageIndex;
    this.size = event.pageSize;
    this.list();
  }

}
