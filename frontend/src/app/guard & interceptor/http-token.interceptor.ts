import { HttpInterceptorFn } from "@angular/common/http";
import { inject } from "@angular/core";
import { KeycloakService } from "../services/keycloak.service";

/*
* This interceptor is used to add the token to the header of the request.
* The token is taken from the keycloak service.
* Through the token, the backend can identify the user and authorize the request.
*/

export const httpTokenInterceptor: HttpInterceptorFn = (req, next) => {
    const keycloakService = inject(KeycloakService);
    const token = keycloakService.keycloak.token;
        if(token) {
            req = req.clone({
                setHeaders: {
                    Authorization: `Bearer ${token}`
                }
            });
        }
        return next(req);
}
