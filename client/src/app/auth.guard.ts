import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';

// type CanActivateFn = (
//   route: ActivatedRouteSnapshot,
//   state: RouterStateSnapshot
// ) => boolean;

export function authGuard(router: Router): CanActivateFn {
  return (route, state) => {
    console.log("checking for token...")
      const token = localStorage.getItem('token');

      // If there's no token, the user is not authenticated.
      if (!token) {
        console.log("token does not exist...")
          router.navigate(['/login']);
          return false;
      }

      // Check if the token has expired.
      const helper = new JwtHelperService();
      if (helper.isTokenExpired(token)) {
        console.log("token is expired...")
          // Token has expired, remove it from localStorage and return false.
          localStorage.removeItem('token');
          router.navigate(['/login']);
          return false;
      }

      console.log("authorisation valid...going to checkout...")
      // If there's a token and it's not expired, the user is authenticated.
      return true;
  };
}