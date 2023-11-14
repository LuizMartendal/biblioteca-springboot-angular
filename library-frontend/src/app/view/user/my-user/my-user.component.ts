import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { lastValueFrom } from 'rxjs';
import { AppNotifyService } from 'src/app/core/app-notify/app-notify.service';
import { UserService } from 'src/app/core/entities/user/user.service';

@Component({
  selector: 'app-my-user',
  templateUrl: './my-user.component.html',
  styleUrls: ['./my-user.component.scss'],
})
export class MyUserComponent  implements OnInit {

  public user: any;

  public genders = [
    'Male',
    'Female'
  ]

  public formUser: FormGroup = new FormGroup({
    firstName: new FormControl('', [Validators.required, Validators.min(3)]),
    lastName: new FormControl('', [Validators.required, Validators.min(3)]),
    username: new FormControl('', [Validators.required, Validators.min(3)]),
    password: new FormControl('', [Validators.required, Validators.min(4)]),
    address: new FormControl('', Validators.required),
    gender: new FormControl('', Validators.required),
    role: new FormControl('')
  });

  constructor(
    private userService: UserService,
    private appNotify: AppNotifyService
  ) { }

  async ngOnInit() {
    this.user = await lastValueFrom(this.userService.loggedUser());

    this.formUser.patchValue(this.user);
    this.formUser.get('password')?.setValue('');
  }

  submit() {
    const user = this.formUser.value;
    this.userService.update(this.user.id, user).subscribe({
      next: () => this.appNotify.successMessage('User updated'),
      error: (error: any) => this.showMessageError(error.error)
    })
  }

  showMessageError(error: any) {
    if (error.errors) {
      for (let i = 0; i < error.errors.length; i++) {
        this.appNotify.errorMessage(error.errors[i].message);
      }
    } else {
      this.appNotify.errorMessage(error.message);
    }
  }
}
