import {Component, OnInit} from '@angular/core';
import {CourseService} from '../../services/services/course.service';
import {DatePipe, NgForOf} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';
import {CourseResponse} from '../../services/models/course-response';
import {MatDialog} from '@angular/material/dialog';
import {AddCourseDialogComponent} from './add-course-dialog/add-course-dialog.component';
import {Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';
import {UserService} from '../../services/services/user.service';

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  imports: [
    NgForOf,
    ReactiveFormsModule
  ],
  styleUrl: './course.component.scss',
  providers: [
    DatePipe
  ]
})
export class CourseComponent implements OnInit {
  courses: CourseResponse[] = [];
  canCrudCourse: boolean = false;

  pageNumber: number = 1;
  pageSize: number = 10;
  totalPages: number = 1;

  constructor(
    private courseSvc: CourseService,
    private userSvc: UserService,
    private datePipe: DatePipe,
    public dialog: MatDialog,
    private router: Router
  ) {
  }

  ngOnInit() {
    this.getMe();
    this.loadCourses()
  }

  nextPage() {
    if (this.pageNumber < this.totalPages) {
      this.pageNumber++;
      this.loadCourses();
    }
  }

  prevPage() {
    if (this.pageNumber > 0) {
      this.pageNumber--;
      this.loadCourses();
    }
  }


  openCourse(course: CourseResponse) {
    if (this.canCrudCourse) { // admin
      this.router.navigate(['course-detail', course.id]).then();
    } else { // teacher
      this.router.navigate(['exam', course.id]).then()
    }
  }

  loadCourses() {
    this.courseSvc.courses({page: this.pageNumber, size: this.pageSize}).subscribe((response) => {
      this.courses = response.content ? response.content.map(course => {
        return {
          id: course.id,
          name: course.name,
          startedAt: this.datePipe.transform(course.startedAt, 'd MMMM yyyy, h:mm a') || undefined,
          endedAt: this.datePipe.transform(course.endedAt, 'd MMMM yyyy, h:mm a') || undefined
        };
      }) : [];
      this.totalPages = response.totalPages || 1;
    });
  }

  openAddCourseDialog() {
    const dialogRef = this.dialog.open(AddCourseDialogComponent, {
      width: '400px',
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadCourses();
      }
    });
  }

  deleteCourse(course: any, event: Event) {
    event.stopPropagation(); // Prevent the card click event from firing
    if (confirm(`Are you sure you want to delete "${course.name}"?`)) {
      this.courseSvc.delete1({"course-id": course.id}).subscribe(() => {
        this.loadCourses();
      });
    }
  }

  getMe() {
    this.userSvc.me().subscribe({
      next: (resp) => {
        this.canCrudCourse = resp.role == 'ADMIN';
      },
      error: (err: HttpErrorResponse) => {
        console.log(err.status)
        const httpStatus = err.status;
        if (httpStatus === 428) { // profile completion required
          this.router.navigate(['profile']).then()
        }
        if (httpStatus === 403) { // admin approval required
          this.router.navigate(['auth']).then()
        }
      }
    })
  }
}
