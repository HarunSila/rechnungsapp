import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';
import { environment } from '../../../environments/environment';
import { KeycloakService } from '../../services/keycloak.service';

/*
  This component is responsible for the header of the application.
  It contains a button that redirects to the Keycloak login page and a button that logs out the user.

  Also it contains all other navigation links to the different pages of the application.
*/

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule, MatButtonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  private keycloakService = inject(KeycloakService);
  keycloakUrl = environment.keycloakUrl;

  navigateToKeycloak() {
    window.open(this.keycloakUrl, '_blank');
  }

  async logout() { 
    this.keycloakService.logout();
  }
}
