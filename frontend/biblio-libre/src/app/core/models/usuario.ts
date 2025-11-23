
export class Usuario{
    id:number|null;
    userId:string;
    email:string;


    constructor(id:number|null,
      userId:string,
      email:string
    ){
      this.id=id;
      this.userId=userId;
      this.email=email
    }
}
