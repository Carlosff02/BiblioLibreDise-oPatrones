import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';


import { Observable } from 'rxjs';
import { Usuario } from '../models/usuario';
import { constants } from '../constants/constants';


@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private readonly http = inject(HttpClient);


  setUsuario(usuario:Usuario){
      //console.log(usuario)
    localStorage.setItem(constants.CURRENT_USER, JSON.stringify(usuario));
  }

  getUsuario(): Usuario | null {
    const usuario = localStorage.getItem(constants.CURRENT_USER);
    if (usuario) {
      const parsedUsuario = JSON.parse(usuario);
      return parsedUsuario; // Assuming you want to return the userid
    }
    return null; // Return null if no user is found
  }



  removeUsuario(){

    return localStorage.removeItem(constants.CURRENT_USER);
  }

}
