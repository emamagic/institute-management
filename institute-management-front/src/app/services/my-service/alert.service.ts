import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class AlertService {
  constructor(private toastr: ToastrService) {}

  showError(message: string): void {
    this.toastr.error(message, 'Error', { closeButton: false, timeOut: 5000 });
  }
}
