import { Component, signal } from '@angular/core';

import { RouterOutlet } from '@angular/router';
import { Nav } from '../../../shared/components/nav/nav';
import { Login } from '../../components/login/login';

@Component({
  selector: 'app-home.component',
  imports: [Nav, RouterOutlet, Login],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  isUserLogin = signal(false);
  recibirLogin(event:boolean){
    this.isUserLogin.set(event)
  }
}
