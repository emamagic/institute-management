import {Component, OnInit} from '@angular/core';
import {UserResponse} from '../../services/models/user-response';
import {UserService} from '../../services/services/user.service';
import {NgForOf, NgIf} from '@angular/common';
import {MatDialog} from '@angular/material/dialog';
import {UpdateUserDialogComponent} from './update-user-dialog/update-user-dialog.component';
import {AdminUpdateRequest} from '../../services/models/admin-update-request';
import {FormsModule} from '@angular/forms';
import {PageResponseUserResponse} from '../../services/models/page-response-user-response';
import {debounceTime, distinctUntilChanged, Subject} from 'rxjs';

@Component({
  selector: 'app-user',
  imports: [
    NgIf,
    NgForOf,
    FormsModule
  ],
  templateUrl: './user.component.html',
  styleUrl: './user.component.scss'
})
export class UserComponent implements OnInit{
  users: UserResponse[] = [];
  searchTerm: string = '';
  selectedRole: string = '';
  selectedGender: string = '';
  selectedAge?: number;
  searchSubject = new Subject<string>();
  roles: string[] = ['admin', 'student', 'teacher'];

  pageNumber: number = 1;
  pageSize: number = 10;
  totalPages: number = 2;
  public approvedUser: AdminUpdateRequest = {isApproved: true}

  constructor(private userSvc: UserService, private dialog: MatDialog) {
    this.searchUsers()
  }

  ngOnInit(): void {
    this.searchSubject.pipe(
      debounceTime(500),
      distinctUntilChanged()
    ).subscribe(() => {
      this.searchUsers();
    });
  }

  approveUser(user: UserResponse): void {
    console.log(this.approvedUser)
    this.userSvc.updateAsAdmin({body: this.approvedUser, id: Number(user.id)}).subscribe(() => {
      user.isApproved = true;
    });
  }

  openEditDialog(user: UserResponse): void {
    const dialogRef = this.dialog.open(UpdateUserDialogComponent, {
      width: '400px',
      data: { ...user }
    });

    dialogRef.afterClosed().subscribe((updatedUser) => {
      if (updatedUser) {
        const index = this.users.findIndex((u) => u.id === updatedUser.id);
        if (index !== -1) {
          this.users[index] = updatedUser;
        }
      }
    });
  }

  nextPage() {
    if (this.pageNumber < this.totalPages) {
      this.pageNumber++;
      this.searchUsers();
    }
  }

  prevPage() {
    if (this.pageNumber > 0) {
      this.pageNumber--;
      this.searchUsers();
    }
  }

  onSearchInput() {
    this.searchSubject.next(this.searchTerm);
  }

  searchUsers() {
    const params: any = {
      name: this.searchTerm || undefined,
      email: this.searchTerm || undefined,
      role: this.selectedRole || undefined,
      gender: this.selectedGender || undefined,
      age: this.selectedAge || undefined
    };

    this.userSvc.search({body: params, page: this.pageNumber, size: this.pageSize}).subscribe((response: PageResponseUserResponse) => {
      this.users = response.content || [];
      this.totalPages = response.totalPages || 1;
    });
  }
}
