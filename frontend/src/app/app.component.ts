import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './components/header/header.component';

/*
  This is the root component of the application. It is the main component that holds all the other components.
  It loads the header component and the router outlet component. The router outlet component is used to load the
  components of the different routes in the application. The header component is used to display the header of the
  application. The header component is displayed on all the pages of the application.
*/

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
}
