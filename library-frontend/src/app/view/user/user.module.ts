import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserRoutingModule } from './user-routing.module';
import { IonicModule } from '@ionic/angular';
import { CreateComponent } from './create/create.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    CreateComponent
  ],
  imports: [
    CommonModule,
    IonicModule.forRoot(),
    ReactiveFormsModule,
    FormsModule,
    UserRoutingModule
  ]
})
export class UserModule { }
