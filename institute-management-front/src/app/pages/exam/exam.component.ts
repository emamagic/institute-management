import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {ExamResponse} from '../../services/models/exam-response';
import {ExamService} from '../../services/services/exam.service';
import {ActivatedRoute} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {AddExamDialogComponent} from './add-exam-dialog/add-exam-dialog.component';
import {UpdateExamDialogComponent} from './update-exam-dialog/update-exam-dialog.component';
import {UserService} from '../../services/services/user.service';
import {JwtService} from '../../services/my-service/jwt.service';

@Component({
  selector: 'app-exam',
    imports: [
        NgForOf,
        NgIf
    ],
  templateUrl: './exam.component.html',
  styleUrl: './exam.component.scss'
})
export class ExamComponent implements OnInit {
  userName: string = ''
  exams: ExamResponse[] = [];
  courseId: string = ''
  pageNumber: number = 1;
  pageSize: number = 10;
  totalPages: number = 1;

  constructor(
    private examSvc: ExamService,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private userSvc: UserService,
    private jwtSvc: JwtService
  ) {
  }

  ngOnInit() {
    this.courseId = this.route.snapshot.paramMap.get('id') || '';
    this.getMe()
    this.loadExams()
  }

  openAddExamDialog() {
    const dialogRef = this.dialog.open(AddExamDialogComponent, {
      width: '400px',
      data: this.courseId,
    });

    dialogRef.afterClosed().subscribe((result: ExamResponse) => {
      if (result) {
        this.loadExams()
      }
    });
  }

  openExam(exam: ExamResponse) {
    const dialogRef = this.dialog.open(UpdateExamDialogComponent, {
      width: '400px',
      data: exam,
    });

    dialogRef.afterClosed().subscribe((updatedResp) => {
      if (updatedResp) {
        this.loadExams()
      }
    });
  }

  deleteExam(exam: ExamResponse, event: Event) {
    event.stopPropagation(); // Prevent the card click event from firing
    if (confirm(`Are you sure you want to delete "${exam.title}"?`)) {
      this.examSvc.delete({id: exam.id!}).subscribe(() => {
        this.loadExams();
      });
    }
  }

  nextPage() {
    if (this.pageNumber < this.totalPages) {
      this.pageNumber++;
      this.loadExams();
    }
  }

  prevPage() {
    if (this.pageNumber > 0) {
      this.pageNumber--;
      this.loadExams();
    }
  }

  loadExams() {
    this.examSvc.exams({"course-id": this.courseId, page: this.pageNumber, size: this.pageSize}).subscribe((resp) => {
      this.exams = resp.content!
    })
  }
  getMe() {
    this.userSvc.me().subscribe((resp) => {
      this.userName = resp.name!
    })
  }

  logout() {
    this.jwtSvc.logout()
  }
}
