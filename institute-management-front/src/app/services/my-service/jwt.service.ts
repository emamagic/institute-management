import {Injectable} from '@angular/core';
import {Observable, tap} from 'rxjs';
import {jwtDecode} from 'jwt-decode';
import {LoginResponse} from '../models/login-response';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';
import {LoginRequest} from '../models/login-request';
import {AuthenticationService} from '../services/authentication.service';
import {RegisterRequest} from '../models/register-request';
import {CodeVerificationRequest} from '../models/code-verification-request';

@Injectable({
  providedIn: 'root'
})
export class JwtService {
  private readonly JWT_ACCESS_TOKEN = 'JWT_ACCESS_TOKEN'
  private readonly JWT_REFRESH_TOKEN = 'JWT_REFRESH_TOKEN'

  constructor(
    private authSvc: AuthenticationService,
    private http: HttpClient,
    private router: Router
  ) {
  }

  public storeJwtToken(token: LoginResponse) {
    if (!token || !token.accessToken || !token.refreshToken) {
      console.error("Invalid token object:", token);
      return;
    }
    // todo: fix refresh api
    localStorage.setItem(this.JWT_ACCESS_TOKEN, token.refreshToken!);
    localStorage.setItem(this.JWT_REFRESH_TOKEN, token.refreshToken!);
  }

  public getAccessToken() {
    const token = localStorage.getItem(this.JWT_ACCESS_TOKEN) as string
    if (!token || token.trim().length === 0) {
      console.error('No access token found');
      return null;
    }
    return token;
  }

  public getRefreshToken() {
    const token = localStorage.getItem(this.JWT_REFRESH_TOKEN) as string
    if (!token || token.trim().length === 0) {
      console.error('No refresh token found');
      return null;
    }
    return token;
  }

  public isLoggedIn() {
    return this.getAccessToken() && this.getRefreshToken() && !this.isTokenExpired();
  }

  isTokenExpired() {
    const accessToken = this.getAccessToken();
    if (!accessToken) return true;
    const decoded = jwtDecode(accessToken);
    if (!decoded.exp) return true;
    const expirationDate = decoded.exp * 1000;
    const now = new Date().getTime();

    return expirationDate < now;
  }

  refresh(): Observable<LoginResponse> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.getRefreshToken()}`
    });
    return this.http.post<LoginResponse>(
      'https://localhost:8080/api/v1/auth/refresh',
      {},
      {headers}
    );
  }

  logout(): void {
    localStorage.setItem(this.JWT_ACCESS_TOKEN, '');
    localStorage.setItem(this.JWT_REFRESH_TOKEN, '');
    this.router.navigate(['auth']).then()
  }

  login(param: LoginRequest): Observable<LoginResponse> {
    return this.authSvc.login({body: param}).pipe(
      tap(response => this.storeJwtToken(response))
    )
  }

  register(param: RegisterRequest): Observable<void> {
    return this.authSvc.register({body: param})
  }

  verifyCode(param: CodeVerificationRequest): Observable<void> {
    return this.authSvc.verifyCode({body: param})
  }


}
