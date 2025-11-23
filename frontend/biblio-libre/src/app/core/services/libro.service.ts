import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Libro } from '../models/libro';
import { PageLibro } from '../models/page-libro';

@Injectable({
  providedIn: 'root'
})
export class LibroService {
  libroUrl = 'http://localhost:8080/api/libros';

  constructor(private readonly http:HttpClient){

  }

  listarLibrosPopulares():Observable<Libro[]>{
    return this.http.get<Libro[]>(`${this.libroUrl}/populares`)
  }

  buscarPorIdioma(httpParams:{
    idioma:string,
    page:number
  }):Observable<PageLibro>{
    const params = new HttpParams()
    .set('idioma', httpParams.idioma)
    .set('page', httpParams.page)
    return this.http.get<PageLibro>(`${this.libroUrl}/buscar-por-idioma`, {params})
  }

  buscarLibros(httpParams: { q: string; page: number }): Observable<PageLibro> {
  const params = new HttpParams()
    .set('q', httpParams.q)
    .set('page', httpParams.page);

  return this.http.get<PageLibro>(`${this.libroUrl}/buscar`, { params });
}

buscarPorFiltros(params: {
  titulo?: string;
  autor?: string;
  idioma?: string;
  page: number;
}): Observable<PageLibro> {
  let httpParams = new HttpParams()
    .set('page', params.page)
    .set('idioma', params.idioma || '');

  if (params.titulo) httpParams = httpParams.set('titulo', params.titulo);
  if (params.autor) httpParams = httpParams.set('autor', params.autor);

  return this.http.get<PageLibro>(`${this.libroUrl}/buscar`, { params: httpParams });
}


  buscarPorNombre(titulo:string):Observable<Libro>{
    const params = new HttpParams()
      .set('nombreLibro',titulo);
    return this.http.get<Libro>(`${this.libroUrl}/buscar-por-nombre`, {params})
  }

}
