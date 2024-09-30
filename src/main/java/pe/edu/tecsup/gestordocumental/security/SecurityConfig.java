package pe.edu.tecsup.gestordocumental.security;

import org.hibernate.annotations.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import pe.edu.tecsup.gestordocumental.config.OAuth2LoginSuccessHandler;
import pe.edu.tecsup.gestordocumental.models.Roles;
import pe.edu.tecsup.gestordocumental.models.Usuarios;
import pe.edu.tecsup.gestordocumental.models.enums.AppRole;
import pe.edu.tecsup.gestordocumental.repositories.RolesRepository;
import pe.edu.tecsup.gestordocumental.repositories.UsuariosRepository;
import pe.edu.tecsup.gestordocumental.security.jwt.AuthEntryPointJwt;
import pe.edu.tecsup.gestordocumental.security.jwt.AuthTokenFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true,
securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    // En esta parte funciona login sin registro en la bd
    /*@Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizedRequests ->
                        authorizedRequests.anyRequest().authenticated())
                //.formLogin(form -> form.defaultSuccessUrl("/", true));
                .oauth2Login(oauth2 ->
                        oauth2.defaultSuccessUrl("http://localhost:3000/inicio", true));
        return http.build();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(Http Security http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizedRequests -> authorizedRequests
                      //  .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated())
                //.formLogin(form -> form.defaultSuccessUrl("/", true));
                .oauth2Login(oauth2 ->
                        oauth2.defaultSuccessUrl("http://localhost:3000/inicio", true));
        return http.build();
    }
    */

    // PRUEBA DE TESTING

    /*
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((requests) ->
                requests
                        .requestMatchers("/doc").permitAll() // se esta concedinedo permiso
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/admin").denyAll()
                        .requestMatchers("/admin/**").denyAll()
                        .anyRequest().authenticated());
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //http.formLogin(withDefaults());

        http.httpBasic(withDefaults()); // basico
        return http.build();
    }
     */

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    @Lazy
    OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public AuthTokenFilter authenticationFilterTokenJwt() {
        return new AuthTokenFilter();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        // configurando un crsf
        http.csrf(csrf -> csrf.csrfTokenRepository(
                CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/api/auth/public/**"));
        //http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests((requests)
                -> requests
                        .requestMatchers("/api/users").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/csrf-token").permitAll()
                        .requestMatchers("/api/auth/public/**").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> {
                    oauth2.successHandler(oAuth2LoginSuccessHandler);
                });
        http.exceptionHandling(exception ->
                exception.authenticationEntryPoint(unauthorizedHandler));
        http.addFilterBefore(authenticationFilterTokenJwt(),
                UsernamePasswordAuthenticationFilter.class);
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults()); // basico
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // init
    @Bean
    public CommandLineRunner initData(RolesRepository rolesRepository,
                                      UsuariosRepository usuariosRepository) {

        return args ->{
            Roles adminRole = rolesRepository.findBynombreRol(AppRole.administrador)
                    .orElseGet(() -> rolesRepository.save(new Roles(AppRole.administrador)));

            Roles asesorRole = rolesRepository.findBynombreRol(AppRole.asesor)
                    .orElseGet(() -> rolesRepository.save(new Roles(AppRole.asesor)));

            Roles estudentRole = rolesRepository.findBynombreRol(AppRole.estudiante)
                    .orElseGet(() -> rolesRepository.save(new Roles(AppRole.estudiante)));

            Roles monitorRole = rolesRepository.findBynombreRol(AppRole.monitor)
                    .orElseGet(() -> rolesRepository.save(new Roles(AppRole.monitor)));
            Roles revisroRole = rolesRepository.findBynombreRol(AppRole.revisor)
                    .orElseGet(() -> rolesRepository.save(new Roles(AppRole.revisor)));

            // prueba para el registro del role admin
            if (!usuariosRepository.existsBycorreoCorporativo("jeremy.bellido@tecsup.edu.pe")) {
                Usuarios usuariosRolAdmin1 =
                        new Usuarios("jeremy.bellido@tecsup.edu.pe");
                usuariosRolAdmin1.setRoles(adminRole);
                usuariosRepository.save(usuariosRolAdmin1);
            }

        };
    }



}
