import {Routes} from '@angular/router';
import {HomeComponent} from './pages/home/home.component';
import {AuthComponent} from './pages/auth/auth.component';
import {authGuard} from './services/gurd/auth.guard';
import {ProfileComponent} from './pages/profile/profile.component';
import {UserComponent} from './pages/user/user.component';
import {CourseComponent} from './pages/course/course.component';
import {CourseDetailComponent} from './pages/course/course-detail/course-detail.component';
import {ExamComponent} from './pages/exam/exam.component';

export const routes: Routes = [
  {path: '', redirectTo: 'home', pathMatch: 'full'},
  {path: 'auth', title: 'auth', component: AuthComponent},
  {path: 'home', title: 'home', component: HomeComponent, canActivate: [authGuard]},
  {path: 'profile', title: 'profile', component: ProfileComponent, canActivate: [authGuard]},
  {path: 'user', title: 'user', component: UserComponent, canActivate: [authGuard]},
  {path: 'course', title: 'course', component: CourseComponent, canActivate: [authGuard]},
  {path: 'course-detail/:id', title: 'course', component: CourseDetailComponent, canActivate: [authGuard]},
  {path: 'exam/:id', title: 'exam', component: ExamComponent, canActivate: [authGuard]},
];
