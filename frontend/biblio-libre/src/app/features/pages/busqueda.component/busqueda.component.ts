import { Component, effect, inject, OnInit, signal } from '@angular/core';
import { LibroService } from '../../../core/services/libro.service';
import { Libro } from '../../../core/models/libro';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { rxResource } from '@angular/core/rxjs-interop';


@Component({
  selector: 'app-busqueda',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './busqueda.component.html',
  styleUrls: ['./busqueda.component.css']
})
export class BusquedaComponent {
  procesandoDatos = signal<boolean>(false);
  private readonly libroService = inject(LibroService);
  
  titulo = '';
  autor = '';
  idiomaSeleccionado = 'todos';


  librosResource = rxResource({
    stream: () => this.libroService.buscarPorFiltros({
      titulo: this.titulo.trim(),
      autor: this.autor.trim(),
      idioma: this.idiomaSeleccionado,
      page: this.page
    })
  })
  page = 1;

  maxVisible = 5;
  paginasVisibles: number[] = [];

  constructor(private route: ActivatedRoute) {
  effect(() => {
    const data = this.librosResource.value();
    if (!data) return;

    this.actualizarPaginacion();
  });

  this.route.queryParamMap.subscribe(params => {
      this.titulo=params.get('titulo') ?? '';
      this.idiomaSeleccionado=params.get('idioma') ?? 'todos';
    });




}


  buscarLibros() {
    this.librosResource.reload()
    // this.procesandoDatos.set(true);
    // this.libroService.buscarPorFiltros({
    //   titulo: this.titulo.trim(),
    //   autor: this.autor.trim(),
    //   idioma: this.idiomaSeleccionado,
    //   page: this.page
    // }).subscribe({
    //   next: (res) => {
    //     this.libros.set(res.content);

    //     this.totalElementos = res.totalElements;
    //     this.totalPaginas = res.totalPages;
    //     this.actualizarPaginacion();
    //     this.procesandoDatos.set(false);
    //   },
    //   error: (err) => {
    //     this.procesandoDatos.set(false);
    //     console.error(err);
    //     alert('Error al buscar los libros');
    //   }
    // });
  }

  getClaseIdioma(idioma: string) {
  return {
    'text-indigo-600 font-semibold': this.idiomaSeleccionado === idioma,
    'text-gray-700 hover:text-indigo-600': this.idiomaSeleccionado !== idioma
  };
}


  actualizarPaginacion() {

    const totalPages = this.librosResource.value()?.totalPages;
    //console.log(this.librosResource.value()?.content)
    if (totalPages === undefined) return;
    const half = Math.floor(this.maxVisible / 2);
    let start = Math.max(1, this.page - half);
    let end = Math.min(totalPages, start + this.maxVisible - 1);
    if (end - start + 1 < this.maxVisible && start > 1) {
      start = Math.max(1, end - this.maxVisible + 1);
    }
    this.paginasVisibles = [];
    for (let i = start; i <= end; i++) this.paginasVisibles.push(i);
  }


  animacion = '';
  cambiarPagina(nuevaPagina: number) {
    const totalPages = this.librosResource.value()?.totalPages;
    if (totalPages === undefined) return;
    if (nuevaPagina < 1 || nuevaPagina > totalPages || nuevaPagina === this.page) return;

    this.animacion = nuevaPagina > this.page ? 'slide-right' : 'slide-left';
    this.page = nuevaPagina;

    setTimeout(() => this.buscarLibros(), 200);
  }
  get totalPages() {
  return this.librosResource.value()?.totalPages ?? 0;
}

get ultimaPaginaVisible() {
  return this.paginasVisibles[this.paginasVisibles.length - 1];
}


  seleccionarIdioma(idioma: string) {
    if (this.idiomaSeleccionado === idioma) return;
    this.idiomaSeleccionado=idioma;
    this.page = 1;
    this.buscarLibros();
  }
}
