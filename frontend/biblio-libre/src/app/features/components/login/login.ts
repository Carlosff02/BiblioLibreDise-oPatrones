import { Component, inject, output, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  private readonly authService = inject(AuthService);
  loginForm = new FormGroup({
  userIdOrEmail: new FormControl<string>('',{ nonNullable: true, validators:[Validators.required]  }),
  contraseña: new FormControl<string>('', { nonNullable: true, validators:[Validators.required] })
  })
  registerForm = new FormGroup({
    userId:new FormControl('',{ nonNullable: true, validators:[Validators.required]  }),
    contraseña:new FormControl('',{ nonNullable: true, validators:[Validators.required]  }),
    confirmPassword:new FormControl('',{ nonNullable: true, validators:[Validators.required]  }),
    email:new FormControl('',{ nonNullable: true, validators:[Validators.required]  }),
  })

  isUserRegistering = signal(false);

  loginEmitter = output<boolean>();

  onLogin(){



    if(this.loginForm.valid){
      const data = this.loginForm.getRawValue();

      this.authService.onLogin(data).subscribe({
        next:(res)=>{
          this.cerrarLogin();
        },
        error:(err)=>{
          console.error(err);
          Swal.fire({
          icon: 'error',
          title: 'Credenciales incorrectas',
          text: 'Usuario o contraseña incorrectos',
          confirmButtonText: 'Aceptar'
        });
        }
      })
    }
  }
  onRegister() {
  const error = this.passwordMatchValidator(this.registerForm);

  if (error) {
    console.log("Las contraseñas NO coinciden");

    return;
  }

  if (this.registerForm.valid) {
    const { confirmPassword, ...data } = this.registerForm.getRawValue();

    this.authService.onRegister(data).subscribe({
      next:(res)=>{
        this.cerrarLogin();
      }, error:(err)=>{
        console.error(err)
      }})
  }
}

cerrarLogin(){
  this.loginEmitter.emit(false);
}
 passwordMatchValidator(form: FormGroup) {
  const password = form.get('contraseña')?.value;
  const confirm = form.get('confirmPassword')?.value;

  return password === confirm ? null : { passwordMismatch: true };
}

}
