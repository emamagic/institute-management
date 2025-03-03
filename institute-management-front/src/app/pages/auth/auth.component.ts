import {Component} from '@angular/core';
import {CodeInputModule} from 'angular-code-input';
import {FormsModule} from '@angular/forms';
import {NgStyle} from '@angular/common';
import {Router} from '@angular/router';
import {RegisterRequest} from '../../services/models/register-request';
import {LoginRequest} from '../../services/models/login-request';
import {CodeVerificationRequest} from '../../services/models/code-verification-request';
import {JwtService} from '../../services/my-service/jwt.service';
import {HttpErrorResponse} from '@angular/common/http';
import {UserService} from '../../services/services/user.service';

@Component({
  selector: 'app-auth',
  imports: [
    CodeInputModule,
    FormsModule,
    NgStyle
  ],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.scss'
})
export class AuthComponent {
  currentStep: number = 1;
  isCodeComplete = false;

  loginData: LoginRequest = {email: '', password: ''};
  verifyData: CodeVerificationRequest = {code: '', email: ''}
  registerData: RegisterRequest = {email: '', password: '', role: ''};

  constructor(
    private router: Router,
    private jwtSvc: JwtService,
    private userSvc: UserService
  ) {
  }
  getMe() {
    this.userSvc.me().subscribe({
      next: () => {
        this.router.navigate(['home']).then()
      },
      error: (err: HttpErrorResponse) => {
        const httpStatus = err.status;
        if (httpStatus === 428) { // profile completion required
          this.router.navigate(['profile']).then()
        }
      }
    })
  }

  onLogin() {
    console.log(this.loginData)
    this.jwtSvc.login(this.loginData).subscribe({
      next: (() => {
        this.getMe()
      })
    })
  }

  onRegister() {
    this.jwtSvc.register(this.registerData).subscribe({
      next: (() => {
        this.goToVerification()
      })
    })
  }

  onVerify() {
    this.verifyData.email = this.registerData.email
    this.jwtSvc.verifyCode(this.verifyData).subscribe({
      next: (() => {
        this.goToLogin()
      })
    })
  }

  getTranslateValue(): number {
    return (this.currentStep - 1) * 33.33;
  }

  goToRegister() {
    this.currentStep = 2;
  }

  goToLogin() {
    this.currentStep = 1;
  }

  goToVerification() {
    this.currentStep = 3;
  }

  onCodeCompleted(code: string) {
    this.isCodeComplete = code.length === 4;
    this.verifyData.code = code
  }

  onCodeChanged(code: string) {
    this.isCodeComplete = code.length === 4;
  }
}
