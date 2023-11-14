import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BookRoutingModule } from './book-routing.module';
import { ListComponent } from './list/list.component';
import { CreateComponent } from './create/create.component';
import { IonicModule } from '@ionic/angular';
import { SharedModule } from 'src/app/shared/shared.module';


@NgModule({
  declarations: [
    ListComponent,
    CreateComponent
  ],
  imports: [
    CommonModule,
    BookRoutingModule,
    IonicModule.forRoot(),
    SharedModule
  ]
})
export class BookModule { }
