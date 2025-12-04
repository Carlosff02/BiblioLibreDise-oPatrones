package com.example.biblo.domain.service;

public interface ILibroPaginaUsuarioService {

    String buscarPaginaPorUsuarioYLibro(String titulo, Long usuarioId);

    void guardarPaginaPorUsuarioYLibro(String titulo, Long usuarioId,String cfi);
}
