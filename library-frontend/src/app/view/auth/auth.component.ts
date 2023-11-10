import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AppStorageService } from 'src/app/core/app-storage/app-storage.service';
import { UserService } from 'src/app/core/entities/user/user.service';
import { Token } from 'src/app/interfaces/util/Token';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss'],
})
export class AuthComponent  implements OnInit {

  public formAuth: FormGroup = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required, Validators.min(4)])
  })

  constructor(
    private userService: UserService,
    private appStorage: AppStorageService
  ) { }

  ngOnInit() {}

  submit() {
    const credentials: Credential = this.formAuth.value;

    this.userService.login(credentials).subscribe({
      next: (token: any) => {
        this.appStorage.set(AppStorageService.KEY_STORAGE.token, token)
      },
      error: (error: any) => this.showMessageError(error.error)
    });
  }

  showMessageError(error: any) {
    console.log(error);
    
  }

}
