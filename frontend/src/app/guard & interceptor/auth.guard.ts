import { CanActivateFn, Router } from "@angular/router";
import { KeycloakService } from "../services/keycloak.service";
import { inject } from "@angular/core";

/*
    * Guard to check if the user is authenticated
    * If the user is not authenticated, redirect to the login page.
    * The authentication is done using the keycloak service.
*/

export const AuthGuard: CanActivateFn = () => {
    const router: Router = inject(Router);
    const keycloakService: KeycloakService = inject(KeycloakService);
    if(keycloakService.keycloak?.isTokenExpired()) {
        router.navigate(['/login']);
        return false;
    }
    return true;
}