import { Component, ElementRef, inject, OnInit, signal, ViewChild } from '@angular/core';
import { Libro } from '../../../core/models/libro';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { LibroService } from '../../../core/services/libro.service';
import { EpubViewerService } from '../../../core/services/epub-viewer.service';
import { UsuarioService } from '../../../core/services/usuario.service';

@Component({
  selector: 'app-visor-epub',
  imports: [RouterLink],
  templateUrl: './visor-epub.html',
  styleUrl: './visor-epub.css'
})
export class VisorEpub implements OnInit {
  private readonly usuarioService = inject(UsuarioService);
  titulo = '';
  libroVisualizado = signal<Libro | null>(null);
  usuarioId:number|null = this.usuarioService.getUsuario()?.id ??  null;
  @ViewChild('viewer', { static: true }) viewerRef!: ElementRef<HTMLDivElement>;

  constructor(
    private route: ActivatedRoute,
    private libroService: LibroService,
    private epubViewer: EpubViewerService
  ) {}

  ngOnInit(): void {
    this.titulo = this.route.snapshot.paramMap.get('titulo')!;
    this.buscarLibroPorTitulo();
  }

  buscarLibroPorTitulo() {
    this.libroService.buscarPorNombre(this.titulo).subscribe({
      next: (res) => {
        this.libroVisualizado.set(res);

        setTimeout(() => {
       
        if(this.usuarioId){
        this.epubViewer.visualizarEpub(res.epub, this.viewerRef, this.titulo,this.usuarioId);
      } else{
      this.epubViewer.visualizarEpub(res.epub, this.viewerRef, this.titulo);
      }
      }, 200);
      },
      error: (err) => console.error(err)
    });
  }

  visualizarEpub() {
    const url = this.libroVisualizado()?.epub;
    if (url) {
      if(this.usuarioId){
        this.epubViewer.visualizarEpub(url, this.viewerRef, this.titulo,this.usuarioId);
      } else{
      this.epubViewer.visualizarEpub(url, this.viewerRef, this.titulo);
      }
    }
  }

  goPrev() {
    this.epubViewer.goPrev();
  }

  goNext() {
    this.epubViewer.goNext();
  }
}
