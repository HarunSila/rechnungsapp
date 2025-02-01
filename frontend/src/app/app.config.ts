import { APP_INITIALIZER, ApplicationConfig, LOCALE_ID, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations';
import { routes } from './app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { KeycloakService } from './services/keycloak.service';
import { httpTokenInterceptor  } from './guard & interceptor/http-token.interceptor';

/*
  * The application configuration is a set of providers that are used to configure the application.
  * The configuration is passed to the application bootstrap function.
  * 
  * Routes are provided by the provideRouter function.
  * HttpClient is provided by the provideHttpClient function.
  * The httpTokenInterceptor is provided by the withInterceptors function.
  * 
  * KeycloakService is provided by the APP_INITIALIZER token. This token is used to provide a function that is executed when the application is initialized.
  * The function is used to initialize the KeycloakService on application startup.
*/

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }), 
    provideRouter(routes), 
    provideAnimations(),
    provideHttpClient(withInterceptors([httpTokenInterceptor])),
    {
      provide: APP_INITIALIZER,
      deps: [KeycloakService],
      useFactory: (keycloakService: KeycloakService) => () => keycloakService.init(),
      multi: true
    }
  ],
};
