import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

    constructor(private router: Router) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        const token = localStorage.getItem('token');

        if (!token) {
            this.router.navigate(['/login']);
            return false;
        }

        const helper = new JwtHelperService();
        try {
            if (helper.isTokenExpired(token)) {
                localStorage.removeItem('token');
                this.router.navigate(['/login']);
                return false;
            }
        } catch (error) {
            console.error('Invalid token:', error);
            localStorage.removeItem('token');
            this.router.navigate(['/login']);
            return false;
        }

        return true;
    }
}