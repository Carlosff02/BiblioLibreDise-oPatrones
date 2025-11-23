export interface ILogin {

    userIdOrEmail: string;
    contraseña: string;
}
export interface AuthenticationRequest {
  userId:string;
  email:string;
  contraseña:string;
}
export interface ILoginResponse {
    id:number;
    token: string;

}
