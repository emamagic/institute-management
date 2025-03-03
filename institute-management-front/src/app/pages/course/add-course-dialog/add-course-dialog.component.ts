import { Component } from '@angular/core';
import {MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle} from '@angular/material/dialog';
import {MatFormField} from '@angular/material/form-field';
import {FormsModule} from '@angular/forms';
import {MatInput, MatInputModule} from '@angular/material/input';
import {MatButton} from '@angular/material/button';
import {CourseService} from '../../../services/services/course.service';
import {CourseRequest} from '../../../services/models/course-request';

@Component({
  selector: 'app-add-course-dialog',
  imports: [
    MatDialogContent,
    MatFormField,
    FormsModule,
    MatInput,
    MatDialogActions,
    MatButton,
    MatInputModule,
    MatDialogTitle
  ],
  templateUrl: './add-course-dialog.component.html',
  styleUrl: './add-course-dialog.component.scss'
})
export class AddCourseDialogComponent {
  course: CourseRequest = { name: '', startedAt: '', endedAt: '' };

  constructor(
    private dialogRef: MatDialogRef<AddCourseDialogComponent>,
    private courseSvc: CourseService
  ) {}

  onCancel() {
    this.dialogRef.close();
  }

  onSave() {
    if (this.course.name && this.course.startedAt && this.course.endedAt) {
      this.course.startedAt = this.convertToLocalDateTime(this.course.startedAt)!
      this.course.endedAt = this.convertToLocalDateTime(this.course.endedAt)!
      this.courseSvc.create1({body: this.course}).subscribe(() => {
        this.dialogRef.close(this.course);
      })
    }
  }

  convertToLocalDateTime(date: string | null): string | null {
    return date ? date + 'T00:00:00' : null;
  }
}
