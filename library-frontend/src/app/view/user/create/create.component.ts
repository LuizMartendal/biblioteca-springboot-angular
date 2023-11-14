import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AppNotifyService } from 'src/app/core/app-notify/app-notify.service';
import { UserService } from 'src/app/core/entities/user/user.service';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.scss'],
})
export class CreateComponent  implements OnInit {

  public genders = [
    {value: 'Male'},
    {value: 'Female'}
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
    private appNotify: AppNotifyService,
    private router: Router
  ) { }

  ngOnInit() {}

  submit() {
    const user = this.formUser.value;
    user.role = 'USER';

    this.userService.create(user).subscribe({
      next: () => {
        this.appNotify.successMessage('Created!');
        this.router.navigate(['/login']);
      },
      error: (error: any) => this.showMessageError(error.error)
    });
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
