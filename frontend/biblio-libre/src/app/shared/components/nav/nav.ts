import { Component, inject, output } from '@angular/core';
import { TokenService } from '../../../core/services/token.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-nav',
  imports: [],
  templateUrl: './nav.html',
  styleUrl: './nav.css'
})
export class Nav {
  readonly tokenService = inject(TokenService);
  private readonly authService = inject(AuthService);
  loginEmitter = output<boolean>();

  activarInicioSesion(){
    this.loginEmitter.emit(true);

  }

  cerrarSesion(){
    this.authService.onLogout()
  }
}
