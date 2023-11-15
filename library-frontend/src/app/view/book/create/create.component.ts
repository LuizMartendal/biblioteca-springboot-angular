import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { lastValueFrom } from 'rxjs';

import { AppNotifyService } from 'src/app/core/app-notify/app-notify.service';
import { BookService } from 'src/app/core/entities/book/book.service';
import { Book } from 'src/app/interfaces/book/Book';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.scss'],
})
export class CreateComponent  implements OnInit {

  public title = 'New Book';
  public button = 'Create';

  public isPopoverOpen = false;

  private book: any;

  public formBook: FormGroup = new FormGroup({
    title: new FormControl('', Validators.required),
    author: new FormControl('', Validators.required),
    launchDate: new FormControl('', Validators.required),
    price: new FormControl('', Validators.required)
  });

  constructor(
    private bookService: BookService,
    private notifyService: AppNotifyService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit() {
    this.activatedRoute.params.subscribe(async (params: any) => {
      this.book = await lastValueFrom(this.bookService.retrieve(params.id));
      if (this.book) {
        this.formBook.patchValue(this.book);
        this.title = 'Updated Book';
        this.button = 'Update';
      } 
    })
  }

  submit() {
    const book: Book = this.formBook.value;

    if (this.book) {
      book.id = this.book.id;
      book.createdBy = this.book.createdBy;
      book.createdIn = this.book.createdIn;

      this.bookService.update(this.book.id, book).subscribe({
        next: () => {
          this.notifyService.successMessage('Book updated');
          this.router.navigate(['/book']);
        },
        error: (error: any) => this.showMessageError(error.error)
      })
    } else {
      this.bookService.create(book).subscribe({
        next: () => {
          this.notifyService.successMessage('Book created');
          this.router.navigate(['/book']);
        },
        error: (error: any) => this.showMessageError(error.error)
      })
    }
  }

  showMessageError(error: any) {
    if (error.errors) {
      for (let i = 0; i < error.errors.length; i++) {
        this.notifyService.errorMessage(error.errors[i].message);
      }
    } else {
      this.notifyService.errorMessage(error.message);
    }
  }

}
