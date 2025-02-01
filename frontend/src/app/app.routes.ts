import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { ProductsComponent } from './components/products/products.component';
import { PartnersComponent } from './components/partners/partners.component';
import { PartnerSelectedComponent } from './components/partners/partner-selected/partner-selected.component';
import { AuthGuard } from './guard & interceptor/auth.guard';

/*
    In this file, we define the routes of our application.
    The routes are defined as an array of Route objects and are protected by the AuthGuard, meaning that the user must be authenticated to access them.
*/

export const routes: Routes = [

    { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
    { path: 'produkte', component: ProductsComponent, canActivate: [AuthGuard] },
    { path: 'geschaeftspartner', component: PartnersComponent, canActivate: [AuthGuard] },
    { path: 'geschaeftspartner/:id', component: PartnerSelectedComponent, canActivate: [AuthGuard] },
    { path: '', redirectTo: '/dashboard', pathMatch: 'full' }
];
