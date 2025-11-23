import { Injectable } from '@angular/core';
import { stat } from 'fs';
import { BehaviorSubject } from 'rxjs';
import { constants } from '../constants/constants';


@Injectable({
  providedIn: 'root'
})
export class TokenService {


  isAuthentication: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(
    false
  );

  constructor() {
    const token = this.getToken();
    if(token){
      this.updateToken(true);
    }
  }
  get isAuth(): boolean {
  return this.isAuthentication.value;
}

  updateToken(status: boolean){
    this.isAuthentication.next(status);
  }
  setToken(token:string){
    this.updateToken(true);

    sessionStorage.setItem(constants.CURRENT_TOKEN, token);
  }

  getToken(): string | null {
    return sessionStorage.getItem(constants.CURRENT_TOKEN) || null
  }



  removeToken(){
    this.updateToken(false);
    return sessionStorage.removeItem(constants.CURRENT_TOKEN);
  }
}
