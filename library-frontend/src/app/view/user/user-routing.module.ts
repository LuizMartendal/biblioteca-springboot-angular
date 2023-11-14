import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateComponent } from './create/create.component';
import { MyUserComponent } from './my-user/my-user.component';
import { AuthGuard } from 'src/app/guards/auth/auth.guard';

const routes: Routes = [
  {
    path: '', redirectTo: 'my-user', pathMatch: 'full'
  },
  {
    path: 'create', component: CreateComponent
  },
  {
    path: 'my-user', component: MyUserComponent, canActivate: [AuthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
