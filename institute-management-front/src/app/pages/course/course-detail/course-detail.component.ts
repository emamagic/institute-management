import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {DatePipe, NgForOf, NgIf} from '@angular/common';
import {CourseResponse} from '../../../services/models/course-response';
import {UserResponse} from '../../../services/models/user-response';
import {CourseService} from '../../../services/services/course.service';
import {MatDialog} from '@angular/material/dialog';
import {UserListDialogComponent} from '../../user/user-list-dialog/user-list-dialog.component';

@Component({
  selector: 'app-course-detail',
  imports: [
    NgForOf,
    NgIf
  ],
  templateUrl: './course-detail.component.html',
  styleUrl: './course-detail.component.scss',
  providers: [
    DatePipe
  ]
})
export class CourseDetailComponent implements OnInit {
  courseId!: string;
  course: CourseResponse = {};
  teacher: UserResponse = {}
  students: UserResponse[] = []

  constructor(
    private courseSvc: CourseService,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) {
  }

  ngOnInit() {
    this.courseId = this.route.snapshot.paramMap.get('id') || '';
    this.loadCourseDetails(this.courseId);
  }

  loadCourseDetails(courseId: string) {
    this.courseSvc.course({"course-id": courseId}).subscribe((course) => {
      this.course.name = course.name
      this.course.startedAt = this.datePipe.transform(course.startedAt, 'd MMMM yyyy, h:mm a') || undefined
      this.course.endedAt = this.datePipe.transform(course.endedAt, 'd MMMM yyyy, h:mm a') || undefined
    })
    this.courseSvc.usersByCourseId({'course-id': courseId}).subscribe((users) => {
      this.teacher = users.find((u) => u.role === 'TEACHER') || {};
      this.students = users.filter((u) => u.role === 'STUDENT');
    })
  }

  openTeacherDialog() {
    this.displayUserListDialog('TEACHER')
  }

  openStudentDialog() {
    this.displayUserListDialog('STUDENT')
  }

  displayUserListDialog(userRole: string) {
    const allUsers: UserResponse[] = []
    allUsers.push(this.teacher)
    allUsers.push(...this.students)
    const dialogRef = this.dialog.open(UserListDialogComponent, {
      width: '300px',
      data: {
        users: allUsers,
        role: userRole,
        courseId: this.courseId
      }
    });

    dialogRef.afterClosed().subscribe(selectedUser => {
      if (selectedUser) {
        this.loadCourseDetails(this.courseId)
      }
    });
  }

  removeStudent(studentId: string) {
    this.courseSvc.withdrawalUser({'course-id': this.courseId, 'user-id': studentId}).subscribe(() => {
      this.loadCourseDetails(this.courseId)
    })
  }
}
