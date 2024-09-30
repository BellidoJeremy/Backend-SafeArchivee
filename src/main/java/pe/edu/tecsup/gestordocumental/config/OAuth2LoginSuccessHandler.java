package pe.edu.tecsup.gestordocumental.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import pe.edu.tecsup.gestordocumental.models.Roles;
import pe.edu.tecsup.gestordocumental.models.Usuarios;
import pe.edu.tecsup.gestordocumental.models.enums.AppRole;
import pe.edu.tecsup.gestordocumental.repositories.RolesRepository;
import pe.edu.tecsup.gestordocumental.security.jwt.JwtUtils;
import pe.edu.tecsup.gestordocumental.security.services.UserDetailsImpl;
import pe.edu.tecsup.gestordocumental.services.UsuarioService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Value("${frontend.url}")
    private String frontendUrl;

    // two attributes
    String username;
    String idAttributeKey;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response, Authentication authentication
    ) throws IOException, ServletException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken =
                (OAuth2AuthenticationToken) authentication;
        if ("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {

            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            // email
            String email = attributes.getOrDefault("email", "").toString();

            if ("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
                username = email.split("@")[0];
                idAttributeKey = "sub";
            } else {
                username = "";
                idAttributeKey = "id";
            }
            System.out.println("HELLO 0AUTH: " + email + " : " + username);

            usuarioService.findBycorreoCorporativo(email)
                    .ifPresentOrElse(usuarios -> {
                        DefaultOAuth2User oAuth2User = new DefaultOAuth2User(
                                List.of(new SimpleGrantedAuthority(usuarios.getRoles()
                                        .getNombreRol().name())),
                                attributes,
                                idAttributeKey
                        );
                        Authentication securityAuth = new OAuth2AuthenticationToken(
                                oAuth2User,
                                List.of(new SimpleGrantedAuthority(usuarios.getRoles().getNombreRol().name())),
                                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()
                        );
                        SecurityContextHolder.getContext().setAuthentication(securityAuth);
                    }, () ->{
                        Usuarios newUsuario = new Usuarios();

                        Optional<Roles> usuarioRole =
                                rolesRepository.findBynombreRol(AppRole.estudiante);
                        if (usuarioRole.isPresent()) {
                            newUsuario.setRoles(usuarioRole.get());
                        } else {
                            //
                            throw new RuntimeException("Default rol not function");
                        }

                        newUsuario.setCorreoCorporativo(email);
                        usuarioService.registerUsuario(newUsuario);

                        DefaultOAuth2User oAuth2User = new DefaultOAuth2User(
                                List.of(new SimpleGrantedAuthority(newUsuario.getRoles().getNombreRol().name())),
                                attributes,
                                idAttributeKey
                        );

                        Authentication securityAuth = new OAuth2AuthenticationToken(
                                oAuth2User,
                                List.of(new SimpleGrantedAuthority(newUsuario.getRoles().getNombreRol().name())),
                                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()
                        );

                        SecurityContextHolder.getContext().setAuthentication(securityAuth);
                    });
        }
        this.setAlwaysUseDefaultTargetUrl(true);

        // JWT TOKEN LOGIC
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Extract necessary attributes
        String email = (String) attributes.get("email");
        System.out.println("OAuth2LoginSuccessHandler: " + username + " : " + email);

        Set<SimpleGrantedAuthority> authorities = new HashSet<>(oAuth2User.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList()));

        Usuarios usuarios = usuarioService.findBycorreoCorporativo(email).orElseThrow(
                () -> new RuntimeException("User not founf"));

        authorities.add(new SimpleGrantedAuthority(usuarios.getRoles().getNombreRol().name()));

        // Create UserDetailsImplm instance
        UserDetailsImpl userDetails = new UserDetailsImpl(
                null,
                email,
                authorities
        );

        // Generate JWT TOken
        String jwtoken = jwtUtils.generateTokenFromUsername(userDetails);

        // Redirect to the frontend with the JWT Token
        String targetUrl = UriComponentsBuilder.fromHttpUrl(frontendUrl + "/oauth2/redirect")
                .queryParam("token", jwtoken)
                .build().toUriString();
        this.setDefaultTargetUrl(targetUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
