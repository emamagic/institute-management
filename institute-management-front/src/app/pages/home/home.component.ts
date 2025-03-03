import {Component} from '@angular/core';
import {UserService} from '../../services/services/user.service';
import {Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';
import {NgIf} from '@angular/common';
import {JwtService} from '../../services/my-service/jwt.service';
import {CourseComponent} from '../course/course.component';
import {UserComponent} from '../user/user.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  imports: [
    NgIf,
    CourseComponent,
    UserComponent
  ],
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  userName: string = ''
  activeTab: string = 'courses';
  isAdmin: boolean = false

  constructor(
    private userSvc: UserService,
    private router: Router,
    private jwtSvc: JwtService
  ) {
    this.getMe()
  }

  setActiveTab(tab: string) {
    this.activeTab = tab;
  }

  getMe() {
    this.userSvc.me().subscribe({
      next: (resp) => {
        this.userName = resp.name!
        if (resp.role === 'ADMIN') {
          this.isAdmin = true
        } else if (resp.role === 'TEACHER') {
          this.isAdmin = false
        } else if (resp.role === 'STUDENT') {

        }
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

  logout() {
    this.jwtSvc.logout()
  }
}
