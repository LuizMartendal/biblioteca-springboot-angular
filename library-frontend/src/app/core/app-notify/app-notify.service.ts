import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class AppNotifyService {

  constructor(
    private toastrService: ToastrService
  ) { }

  successMessage(message: string) {
    this.toastrService.success(message, 'Sucesso', {
      progressBar: true,
      timeOut: 10000,
      positionClass: 'toast-bottom-right',
    })
  }

  errorMessage(message: string) {
    this.toastrService.error(message, 'Erro', {
      progressBar: true,
      timeOut: 10000,
      positionClass: 'toast-bottom-right'
    })
  }
}
