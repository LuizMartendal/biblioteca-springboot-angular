import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserRoutingModule } from './user-routing.module';
import { IonicModule } from '@ionic/angular';
import { CreateComponent } from './create/create.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MyUserComponent } from './my-user/my-user.component';


@NgModule({
  declarations: [
    CreateComponent,
    MyUserComponent
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
