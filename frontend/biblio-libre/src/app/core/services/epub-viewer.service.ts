import { ElementRef, inject, Injectable } from '@angular/core';
import ePub, { Book, Rendition } from 'epubjs';
import { constants } from '../constants/constants';
import { ArchivoResponseDTO } from '../models/archivo-response-dto.model';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class EpubViewerService {
  private readonly http = inject(HttpClient);
  private book?: Book;
  private rendition?: Rendition;
  titulo='';
  usuarioId:number|null=null;
  public currentCfi: string | null = null;
  public isAtEnd = false;
  public isAtStart = true;
  apiUrl = 'http://localhost:8080/api/visor-epub';

  constructor() {}

  guardarVisualizacionUsuario(usuarioId:number,cfi:string){
    const params = new HttpParams()
    .set('titulo', this.titulo)
    .set('usuarioId', usuarioId)
    .set('cfi', cfi)
    return this.http.get(this.apiUrl, {params});
  }

  visualizarEpub(epubUrl: string, viewerRef: ElementRef, titulo: string, usuarioId?: number): void {
    if (!epubUrl) return;
    this.titulo=titulo;
    let proxyUrl = `${constants.apiUrl}/visor-epub/epub?url=${encodeURIComponent(epubUrl)}&titulo=${titulo}`;
    if (usuarioId) {
      proxyUrl += `&usuarioId=${usuarioId}`;
      this.usuarioId=usuarioId;
    }

    fetch(proxyUrl)
      .then(res => res.json())
      .then((dto: ArchivoResponseDTO) => {
        const binary = atob(dto.data);
        const bytes = new Uint8Array(binary.length);
        for (let i = 0; i < binary.length; i++) {
          bytes[i] = binary.charCodeAt(i);
        }

        this.book = ePub(bytes.buffer);

        this.rendition = this.book.renderTo(viewerRef.nativeElement, {
          width: '100%',
          height: '100%',
          allowScriptedContent: true
        });

        this.rendition.on("relocated", (location: any) => {
          this.currentCfi = location.start.cfi;
          this.isAtEnd = location.atEnd;
          this.isAtStart = location.atStart;
          this.guardarCfiActual();
        });

        if (dto.cfi && dto.cfi.length > 0) {
          this.currentCfi = dto.cfi;
          this.rendition.display(dto.cfi);
        } else {

            this.rendition.display();

        }
      })
      .catch(err => console.error('Error cargando EPUB:', err));
  }

  goPrev(): void {
    if (this.isAtStart) return;
    this.rendition?.prev();
  }

  goNext(): void {
    if (this.isAtEnd) return;
    this.rendition?.next();
  }

  guardarCfiActual(): void {
    if (this.currentCfi&&this.usuarioId) {
      this.guardarVisualizacionUsuario(this.usuarioId,this.currentCfi).subscribe({
        error:(err)=>{
          console.error(err);
        }
      })
    }
  }

  getCfiGuardado(): string | null {
    return localStorage.getItem(constants.ACTUAL_PAGE);
  }
}
