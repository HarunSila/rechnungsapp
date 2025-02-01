import { Directive, Input, TemplateRef, ViewContainerRef } from "@angular/core";
import { KeycloakService } from "../services/keycloak.service";

/* 
    This directive is used to show or hide elements based on the user's roles.
    It is used in the HTML template of the components.
    The directive checks if the user has the role specified in the input.
    If the user has the role, the element is shown, otherwise it is hidden.
*/

@Directive({
    selector: '[appHasRole]',
    standalone: true
})
export class HasRoleDirective {
    private hasView = false;

    constructor(
        private templateRef: TemplateRef<any>,
        private viewContainr: ViewContainerRef,
        private keycloakService: KeycloakService
    ) {}

    // This method is called when the directive is used in the HTML template.
    // It checks if the user has the role specified in the input.
    // If the user has the role and the element is not shown, the element is shown.
    // If the user does not have the role and the element is shown, the element is hidden.
    @Input() set appHasRole(role: string) {
        if (this.keycloakService.hasRoles(role) && !this.hasView) {
            this.viewContainr.createEmbeddedView(this.templateRef);
            this.hasView = true;
        } else if (!this.keycloakService.hasRoles(role) && this.hasView) {
            this.viewContainr.clear();
            this.hasView = false;
        }
    }
}