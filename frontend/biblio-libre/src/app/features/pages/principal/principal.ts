import { Component, inject, signal } from '@angular/core';
import { LibroService } from '../../../core/services/libro.service';
import { RouterLink } from '@angular/router';
import { rxResource } from '@angular/core/rxjs-interop';
import { Login } from '../../components/login/login';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-principal',
  imports: [RouterLink, FormsModule],
  templateUrl: './principal.html',
  styleUrl: './principal.css'
})
export class Principal {

  private readonly libroService = inject(LibroService)
  titulo='';
  libros = rxResource({
    stream: () => this.libroService.listarLibrosPopulares()
});



}
