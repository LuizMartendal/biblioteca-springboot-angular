import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { AppStorageService } from 'src/app/core/app-storage/app-storage.service';
import { Book } from 'src/app/interfaces/book/Book';
import { Token } from 'src/app/interfaces/util/Token';

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrls: ['./book-card.component.scss'],
})
export class BookCardComponent  implements OnInit {

  @Input() book: Book | undefined;

  @Output() edit = new EventEmitter<string>();
  @Output() delete = new EventEmitter<string>();

  public user: any;

  constructor(
    private appStorage: AppStorageService
  ) { }

  ngOnInit() {
    this.user = (this.appStorage.get(AppStorageService.KEY_STORAGE.token) as Token).user;
    console.log(this.user);
    
  }

  public editBook() {
    if (this.book) {
      this.edit.emit(this.book.id);
    }
  }

  public deleteBook() {
    if (this.book) {
      this.delete.emit(this.book.id);
    }
  }

}
