package com.auth.auth.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth.auth.dto.AuthenticationResponse;
import com.auth.auth.dto.ModuloDto;
import com.auth.auth.dto.PerfilDto;
import com.auth.auth.dto.PermisoDto;
import com.auth.auth.dto.SistemaDto;
import com.auth.auth.entities.Perfil;
import com.auth.auth.entities.Usuario;
import com.auth.auth.repositories.UsuarioRepository;
import com.auth.auth.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.auth.auth.security.TokenJwtConfig.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
            UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        Usuario user = null;
        String username = null;
        String password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
            username = user.getUsername();
            password = user.getPassword();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);

        return authenticationManager.authenticate(authenticationToken);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        User user = (User) authResult.getPrincipal();
        String username = user.getUsername();
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("no existe el usuario"));

        List<Perfil> perfilesUsuario = usuario.getPerfiles();

        List<PerfilDto> perfilesDto = perfilesUsuario.stream().map(perfil -> {
            PerfilDto dto = new PerfilDto();
            dto.setId(perfil.getId());
            dto.setNombre(perfil.getNombre());

            // Ahora perfil.getSistemas() devuelve un Set<Sistema>
            Set<SistemaDto> sistemasDto = perfil.getSistemas().stream().map(sistema -> {
                SistemaDto sistemaDto = new SistemaDto();
                sistemaDto.setNombreSistema(sistema.getNombre());
                sistemaDto.setCodSistema(sistema.getCodigo()); // Ajusta si usas otro nombre de campo

                Set<ModuloDto> modulosDto = sistema.getModulos().stream().map(modulo -> {
                    ModuloDto moduloDto = new ModuloDto();
                    moduloDto.setIdModulo(modulo.getId());
                    moduloDto.setNombreModulo(modulo.getNombre());

                    Set<PermisoDto> permisoDtos = modulo.getPermisos().stream()
                            .map(permiso -> new PermisoDto(permiso.getId(), permiso.getNombre()))
                            .collect(Collectors.toSet());

                    moduloDto.setPermisos(permisoDtos);
                    return moduloDto;
                }).collect(Collectors.toSet());

                sistemaDto.setModulos(modulosDto);
                return sistemaDto;
            }).collect(Collectors.toSet());

            dto.setSistemas(sistemasDto);
            return dto;
        }).toList();

        Claims claims = Jwts.claims()
                .add("authorities", new ObjectMapper().writeValueAsString(roles))
                .add("username", username).build();

        SecretKey secretKey = jwtUtils.getSecretKey();

        String token = Jwts.builder()
                .subject(username)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .issuedAt(new Date())
                .signWith(secretKey)
                .compact();

        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

        boolean isFunc = roles.stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_FUNC"));

        AuthenticationResponse body = new AuthenticationResponse(token, true, isFunc, perfilesDto);

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(200);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        AuthenticationResponse body = new AuthenticationResponse("", false, false);

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType(CONTENT_TYPE);

    }

}
