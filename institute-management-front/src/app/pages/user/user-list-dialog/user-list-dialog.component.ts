import {Component, Inject} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NgForOf} from '@angular/common';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {UserResponse} from '../../../services/models/user-response';
import {UserService} from '../../../services/services/user.service';
import {CourseService} from '../../../services/services/course.service';

@Component({
  selector: 'app-user-list-dialog',
  imports: [
    FormsModule,
    NgForOf
  ],
  templateUrl: './user-list-dialog.component.html',
  styleUrl: './user-list-dialog.component.scss'
})
export class UserListDialogComponent {
  users: UserResponse[] = []
  courseId: string = ''

  constructor(
    public dialogRef: MatDialogRef<UserListDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { users: UserResponse[]; role: string ; courseId: string},
    private userSvc: UserService,
    private courseSvc: CourseService
  ) {
    this.courseId = data.courseId
    console.log(data.users)
    this.loadUsers(data.role, data.users)
  }

  loadUsers(role: string, joinedUsers: UserResponse[]) {
    this.userSvc.users().subscribe((users) => {
      this.users = users
        .filter((u) => u.isVerified && u.isApproved)
        .filter((u) => u.role === role)
        .filter((u) => !joinedUsers.some((cu) => cu.id === u.id))
    })
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  selectUser(user: UserResponse): void {
    this.courseSvc.enrollUser({"course-id": this.courseId, 'user-id': user.id!}).subscribe(() => {
      this.dialogRef.close(user);
    })
  }
}
