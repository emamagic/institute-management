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
import {ExamResponse} from '../../../services/models/exam-response';
import {ExamService} from '../../../services/services/exam.service';
import {ExamRequest} from '../../../services/models/exam-request';

@Component({
  selector: 'app-update-exam-dialog',
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
  templateUrl: './update-exam-dialog.component.html',
  styleUrl: './update-exam-dialog.component.scss'
})
export class UpdateExamDialogComponent {
  updatedExam: ExamRequest = {courseId: '', title: '', duration: '', description: ''}
  exam: ExamResponse = {}
  originExam: ExamResponse = {}

  constructor(
    public dialogRef: MatDialogRef<UpdateExamDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ExamResponse,
    private examSvc: ExamService
  ) {
    this.exam = {...data};
    this.originExam = {...data}
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  getUpdatedFields() {
    Object.keys(this.exam).forEach((key) => {
      if (this.exam[key as keyof ExamResponse] !== this.originExam[key as keyof ExamResponse]) {
        (this.updatedExam as any)[key] = this.exam[key as keyof ExamResponse];
      }
    });

    return this.updatedExam;
  }

  onUpdate(): void {
    if (this.exam.title && this.exam.description && this.exam.duration) {
      this.updatedExam.duration = String(this.exam.duration);
      this.examSvc.update({body: this.getUpdatedFields(), id: this.exam.id!}).subscribe(() => {
        this.dialogRef.close(this.exam);
      })
    }
  }
}
