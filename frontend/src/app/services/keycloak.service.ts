import { Injectable } from "@angular/core";
import Keycloak from "keycloak-js";
import { environment } from "../../environments/environment";
import { UserProfile } from "../models/user-profile";

/*
    This service is used to handle the Keycloak authentication.
    It initializes the Keycloak instance and checks if the user is authenticated.
    It also provides methods to login, logout and get the user roles or other information.
    The URL, realm and client ID are defined in the environment file.
*/

@Injectable({
    providedIn: "root"
})
export class KeycloakService {

    private _keycloak!: Keycloak;
    private _profile!: UserProfile;

    // Getter for Keycloak instance
    get keycloak() {
        if (!this._keycloak) {
            this._keycloak = new Keycloak({
                url: environment.keycloakUrl,
                realm: environment.realm,
                clientId: environment.clientId,
            });
        }
        return this._keycloak;
    }

    // Getter for the profile
    get profile() {
        return this._profile;
    }

    // Initialize Keycloak and check if the user is authenticated
    async init() {
        // Check if the Keycloak instance is already authenticated
        let authenticated = this.keycloak?.authenticated;

        // If not authenticated, initialize Keycloak and perform the login
        if(!authenticated){
            authenticated = await this.keycloak.init({
                onLoad: "login-required",   // Force login if not authenticated
            });
        }

        // If authenticated, load the user profile and assign the token
        if(authenticated){
            this._profile = (await this.keycloak?.loadUserProfile()) as UserProfile;
            this._profile.token = this.keycloak?.token;
        }
    }

    // Check if the user is authenticated
    isLoggedIn() {
        return this.keycloak.authenticated;
    }

    // Get the user roles from the token
    // for realm roles use realm_access.roles
    getRoles() {
        return this.keycloak?.tokenParsed?.resource_access?.[environment.clientId]?.roles || [];
    }

    // Check if the user has a specific role
    hasRoles(role: string) {
        return this.getRoles().includes(role);
    }

    // Login and logout methods
    login() {
        this.keycloak.login();
    }
    logout() {
        this.keycloak.logout();
    }
}