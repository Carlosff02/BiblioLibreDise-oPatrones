package com.example.biblo.infraestructure.security;



import com.example.biblo.application.dto.AuthenticationRequest;
import com.example.biblo.application.dto.LoginRequest;
import com.example.biblo.application.service.EmailService;
import com.example.biblo.domain.exceptions.UserIdOrEmailDuplicatedException;
import com.example.biblo.domain.models.AuthenticationResponse;
import com.example.biblo.domain.models.Usuario;
import com.example.biblo.infraestructure.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final EmailService emailService;
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService jwtService;
    private final AuthenticationManager authenticationManager;



    public AuthenticationResponse register(AuthenticationRequest request){
        Usuario usuario = Usuario.builder()
                        .contraseña(passwordEncoder.encode(request.contraseña()))
                                .userId(request.userId())
                                        .email(request.email())
                                                .build();

        Optional<Usuario> usuarioBuscar = repository.findByUserIdOrEmail(request.userId(), request.email());
        if(usuarioBuscar.isPresent()){
            throw new UserIdOrEmailDuplicatedException(usuarioBuscar.get().getUserId());
        }
        usuario =repository.save(usuario);

        String token = jwtService.generarToken(usuario);
        String destinatario = usuario.getEmail();
        String subject = "Usuario "+usuario.getUserId()+" creado correctamente";
        String mensaje = "Su usuario ha sido creado correctamente, la contraseña es: "+usuario.getUserId() + " ingrese a la pagina https://www.jachcloud.pe/sanfernando-rrhh/ para actualizarla";
        emailService.enviarCorreoAsync(destinatario,subject,mensaje);

        return new AuthenticationResponse(usuario.getId(),
                token);
    }

    public AuthenticationResponse authenticate(LoginRequest request) {
        Optional<Usuario> user = repository.findByUserIdOrEmail(request.userIdOrEmail(), request.userIdOrEmail());

        if (user.isEmpty()) {
            return null;
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.get().getUserId(),
                        request.contraseña()
                )
        );

        String token = jwtService.generarToken(user.get());


        return new AuthenticationResponse(
                user.get().getId(),
                token
        );
    }


    public boolean cambiarContrasena(String userid, String contrasenaActual, String nuevaCotrasena){
        Optional<Usuario> usuarioOptional = repository.findByUserIdOrEmail(userid,userid);

        if(usuarioOptional.isEmpty()){
            throw  new UsernameNotFoundException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOptional.get();

        if(!passwordEncoder.matches(contrasenaActual, usuario.getPassword())){
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        usuario.setContraseña(passwordEncoder.encode(nuevaCotrasena));
        repository.save(usuario);

        return true;
    }

    public AuthenticationResponse refreshToken(
            HttpServletRequest request,
            String refreshToken) throws IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {

            return null;
        }



        String username = jwtService.extractUsername(refreshToken);

        if(username !=null){

            var userDetails = repository.findByUserIdOrEmail(username,username)
                    .orElseThrow();


            if(jwtService.isValid(refreshToken, userDetails)){
                var accessToken = jwtService.generarToken(userDetails);
                var authResponse = new AuthenticationResponse(userDetails.getId(),
                        accessToken);
                return authResponse;
            }

        }
        return null;
    }
}
