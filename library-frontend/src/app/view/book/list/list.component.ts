import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AlertController, Platform, ViewDidEnter } from '@ionic/angular';
import { delay, finalize } from 'rxjs';
import { AppNotifyService } from 'src/app/core/app-notify/app-notify.service';
import { BookService } from 'src/app/core/entities/book/book.service';
import { PlatformService } from 'src/app/core/platform/platform.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
})
export class ListComponent  implements OnInit, ViewDidEnter {

  public books: any;

  public isLoading = false;
  public isMobile = false;

  public page: any = 0;
  public size: any = 16;

  constructor(
    private bookService: BookService,
    private platformService: PlatformService,
    private router: Router,
    private alertController: AlertController,
    private notifyService: AppNotifyService
  ) { }

  ionViewDidEnter(): void {
    this.ngOnInit();
  }

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
      order: 'title ASC'
    }).pipe(finalize(() => {
      this.isLoading = false;
    })).subscribe((response: any) => {
      this.books = response;
    });
  }

  onPage(event: any) {
    this.page = event.pageIndex;
    this.size = event.pageSize;
    this.list(this.page, this.size);
  }

  goToEdit(id: any) {
    this.router.navigate([`/book/${id}`])
  }

  async delete(id: any) {
    const alert = await this.alertController.create({
      header: 'Warning!',
      message: 'Do you really want to delete this book?',
      buttons: [
        {
          text: 'No',
          htmlAttributes: {
            'aria-label': 'alert dialog',
          },
          role: 'no'
        },
        {
          text: 'Yes',
          htmlAttributes: {
            'aria-label': 'alert dialog',
          },
          role: 'yes',
          handler: () => {
            this.bookService.delete(id)
            .subscribe({
              next: () => {
                this.notifyService.successMessage('Book deleted');
                this.list();
              },
              error: (error: any) => this.notifyService.errorMessage(error.error.message)
            })
          }
        }
      ],
    });
    alert.present();
  }

}
