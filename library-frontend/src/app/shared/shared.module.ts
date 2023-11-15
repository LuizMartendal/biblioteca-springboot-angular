import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookCardComponent } from './book-card/book-card.component';
import { IonicModule } from '@ionic/angular';

import { MatPaginatorModule } from '@angular/material/paginator';

@NgModule({
  declarations: [
    BookCardComponent
  ],
  imports: [
    CommonModule,
    IonicModule.forRoot(),
    MatPaginatorModule
  ],
  exports: [
    BookCardComponent,
    MatPaginatorModule
  ]
})
export class SharedModule { }
