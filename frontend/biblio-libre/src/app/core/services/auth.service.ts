import { inject, Injectable } from '@angular/core';
import { catchError, map, Observable, throwError } from 'rxjs';

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthenticationRequest, ILogin, ILoginResponse } from '../models/auth.model';
import { TokenService } from './token.service';
import { Usuario } from '../models/usuario';
import { constants } from '../constants/constants';
import { UsuarioService } from './usuario.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly usuarioService = inject(UsuarioService);
  private readonly url = `${constants.apiUrl}`
  constructor(private tokenService: TokenService,
    private http:HttpClient,
    private router:Router
  ) { }

  onLogin(data: ILogin) {
  return this.http
    .post<ILoginResponse>(`${this.url}/login`, data)
    .pipe(
      map((response) => {
        if (response) {
          // Guardar tokens
          this.tokenService.setToken(response.token);

          // Crear usuario en memoria
          const usuario = new Usuario(
            response.id,
            data.userIdOrEmail,
            ''
          );
          this.usuarioService.setUsuario(usuario);
        }
        return response;
      }),
      catchError((error) => {
        // 🔥 Capturamos el mensaje devuelto por el backend
        let errorMsg = 'Ocurrió un error inesperado';
        if (error.status === 401 || error.status === 500) {
          errorMsg = error.error; // el backend devuelve texto plano
        }

        // También puedes capturar si el backend devuelve JSON
        if (error.error?.message) {
          errorMsg = error.error.message;
        }

        // Mostrar el mensaje o lanzarlo para que el componente lo maneje
        return throwError(() => new Error(errorMsg));
      })
    );
}
    onRegister(request:AuthenticationRequest){
      return this.http
      .post<ILoginResponse>(`${this.url}/register`, request)
      .pipe(
      map((response) => {
        if (response) {
          this.tokenService.setToken(response.token);

          // Crear usuario en memoria
          const usuario = new Usuario(
            response.id,
            request.userId,
            request.email
          );
          this.usuarioService.setUsuario(usuario);
        }
      }
    )
  )
    }





    onLogout() {
  // Guardamos el alert original por si se necesitara restaurar




   this.tokenService.removeToken();
    localStorage.clear();
    sessionStorage.clear();


}

}
