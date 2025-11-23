package com.example.biblo.infraestructure.security;


import com.example.biblo.domain.models.Usuario;
import com.example.biblo.infraestructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {

            Optional<Usuario> usuario = usuarioRepository.findByUserIdOrEmail(username,username);
            if(usuario.isPresent()){
                return usuario.get();
            }
            throw new RuntimeException("Usuario no encontrado");


                    } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
