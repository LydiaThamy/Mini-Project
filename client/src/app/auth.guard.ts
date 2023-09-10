import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';

type CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => boolean;

export function authGuardFactory(router: Router): CanActivateFn {
  return (route, state) => {
      const token = localStorage.getItem('token');

      // If there's no token, the user is not authenticated.
      if (!token) {
          router.navigate(['/login']);
          return false;
      }

      const helper = new JwtHelperService();

      // Check if the token has expired.
      if (helper.isTokenExpired(token)) {
          // Token has expired, remove it from localStorage and return false.
          localStorage.removeItem('token');
          router.navigate(['/login']);
          return false;
      }

      // If there's a token and it's not expired, the user is authenticated.
      return true;
  };
}