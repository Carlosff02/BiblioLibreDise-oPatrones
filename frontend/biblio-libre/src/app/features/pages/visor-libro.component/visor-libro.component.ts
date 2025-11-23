import { Component, ElementRef, OnInit, signal, ViewChild } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Libro } from '../../../core/models/libro';
import { LibroService } from '../../../core/services/libro.service';
import ePub, { Book, Rendition } from 'epubjs';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-visor-libro.component',
  imports: [RouterLink],
  templateUrl: './visor-libro.component.html',
  styleUrl: './visor-libro.component.css'
})
export class VisorLibroComponent implements OnInit{
    titulo = '';
    procesandoDatos = signal<boolean>(false);
    libroVisualizado = signal<Libro|null>(null);

    constructor(private route: ActivatedRoute,
      private libroService:LibroService
    ){

    }

    ngOnInit(): void {
      this.titulo = this.route.snapshot.paramMap.get('titulo')!;
      this.buscarLibroPorTitulo()
  }

 libroVisualizadoDescarga() {
  return {
    titulo: this.libroVisualizado()?.titulo,
    textPlain: this.libroVisualizado()?.textHtml}
  };



 buscarLibroPorTitulo(){
this.procesandoDatos.set(true);
this.libroService.buscarPorNombre(this.titulo)
  .pipe(finalize(() => this.procesandoDatos.set(false)))
  .subscribe({
      next: res => this.libroVisualizado.set(res),
      error: err => console.error(err)
  });

}

  visualizarEpub(){

  }
  //Transformación de datos
  transformarIdiomaLibro(idioma: string | undefined): string {
    if(idioma){
  switch (idioma.toLowerCase()) {
    case 'es': return 'Español';
    case 'en': return 'Inglés';
    case 'fr': return 'Francés';
    case 'de': return 'Alemán';
    case 'it': return 'Italiano';
    case 'pt': return 'Portugués';
    case 'nl': return 'Neerlandés';
    case 'da': return 'Danés';
    case 'sv': return 'Sueco';
    case 'no': return 'Noruego';
    case 'fi': return 'Finlandés';
    case 'el': return 'Griego';
    case 'la': return 'Latín';
    case 'pl': return 'Polaco';
    case 'ru': return 'Ruso';
    case 'zh': return 'Chino';
    case 'ja': return 'Japonés';
    case 'ar': return 'Árabe';
    case 'hu': return 'Húngaro';
    case 'cs': return 'Checo';
    default: return idioma; // por si no coincide, devuelve el valor original
  }
  }
  return '';
}



}
