import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NgIf} from '@angular/common';
import {UserService} from '../../services/services/user.service';
import {UserUpdateRequest} from '../../services/models/user-update-request';
import {Router} from '@angular/router';

@Component({
  selector: 'app-profile',
  imports: [
    FormsModule,
    NgIf
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent {
  profileRequest: UserUpdateRequest = {name: '', age: '', gender: ''}

  constructor(
    private userSvc: UserService,
    private router: Router
  ) {
  }

  updateProfile() {
    this.userSvc.updateProfile({body: this.profileRequest}).subscribe({
      next: ((resp) => {
          this.router.navigate(['home']).then()
      })
    })
  }

}
