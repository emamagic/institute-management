import {Component, Inject} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import {MatFormField} from '@angular/material/form-field';
import {MatInput, MatInputModule} from '@angular/material/input';
import {FormsModule} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {ExamService} from '../../../services/services/exam.service';
import {ExamRequest} from '../../../services/models/exam-request';

@Component({
  selector: 'app-add-exam-dialog',
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
  templateUrl: './add-exam-dialog.component.html',
  styleUrl: './add-exam-dialog.component.scss'
})
export class AddExamDialogComponent {
  exam: ExamRequest = {courseId: '', title: '', duration: '' as string, description: ''};

  constructor(
    public dialogRef: MatDialogRef<AddExamDialogComponent>,
    private examSvc: ExamService,
    @Inject(MAT_DIALOG_DATA) public courseId: string
  ) {
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    if (this.exam.title && this.exam.description && this.exam.duration) {
      this.exam.courseId = this.courseId
      this.exam.duration = String(this.exam.duration)
      console.log(this.exam)
      this.examSvc.create({body: this.exam}).subscribe(() => {
        this.dialogRef.close(this.exam);
      })
    }
  }
}
