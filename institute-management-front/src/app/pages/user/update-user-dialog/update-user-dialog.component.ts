import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import {UserService} from '../../../services/services/user.service';
import {FormsModule} from '@angular/forms';
import {UserResponse} from '../../../services/models/user-response';

@Component({
  selector: 'app-user-dialog',
  templateUrl: './update-user-dialog.component.html',
  imports: [
    FormsModule
  ],
  styleUrls: ['./update-user-dialog.component.scss']
})
export class UpdateUserDialogComponent {
  originUser: UserResponse;
  updatedUser: UserResponse = {};

  constructor(
    public dialogRef: MatDialogRef<UpdateUserDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public user: UserResponse,
    private userSvc: UserService
  ) {
    this.originUser = { ...user };
  }

  getUpdatedFields() {
    this.updatedUser = {};
    Object.keys(this.user).forEach((key) => {
      if (this.user[key as keyof UserResponse] !== this.originUser[key as keyof UserResponse]) {
        (this.updatedUser as any)[key] = this.user[key as keyof UserResponse];
      }
    });

    return this.updatedUser;
  }

  updateUser(): void {

    this.userSvc.updateAsAdmin({body: this.getUpdatedFields(), id: Number(this.user.id)}).subscribe(
      (resp) => {
      this.dialogRef.close(resp);
    });
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
}
