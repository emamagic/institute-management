import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import {JwtService} from '../my-service/jwt.service';

export const authGuard: CanActivateFn = (route, state) => {
  let authService = inject(JwtService);
  let routerService = inject(Router);
  if (!authService.isLoggedIn()) {
    routerService.navigate(['/auth']).then();
    return false;
  }
  return true;
};
