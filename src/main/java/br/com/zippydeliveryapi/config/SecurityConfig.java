package br.com.zippydeliveryapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.zippydeliveryapi.model.acesso.Usuario;
import br.com.zippydeliveryapi.model.acesso.UsuarioService;
import br.com.zippydeliveryapi.segurança.jwt.JwtAuthenticationEntryPoint;
import br.com.zippydeliveryapi.segurança.jwt.JwtTokenAuthenticationFilter;
import br.com.zippydeliveryapi.segurança.jwt.JwtTokenProvider;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger*/**",
            "/v2/api-docs",
            "/webjars/**",
            "/routes/**",
            "/favicon.ico",
            "/ws/**",
            "/delifacil/**/dadosPedidoNew/**",
            "/delifacil/image/**"
    };

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
            UsuarioService userDetailService) throws Exception {

        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

  
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	
    http
 	.httpBasic().disable().csrf().disable().cors().and().sessionManagement()
 	.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
 	.authenticationEntryPoint(authenticationEntryPoint).and().authorizeRequests()

 	.antMatchers(AUTH_WHITELIST).permitAll()
 	.antMatchers(HttpMethod.POST, "/api/login").permitAll()
	 	
 	.antMatchers(HttpMethod.POST, "/api/cliente").permitAll() //Libera o cadastro de cliente para o cadastro de usuário
 	.antMatchers(HttpMethod.POST, "/api/empresa").permitAll() //Libera o cadastro de empresa para o cadastro de usuário
        
    //.antMatchers(HttpMethod.POST, "/api/produto").hasAnyAuthority(Usuario.ROLE_EMPRESA, Usuario.ROLE_ADMIN) //Cadastro de produto
    //.antMatchers(HttpMethod.PUT, "/api/produto/*").hasAnyAuthority(Usuario.ROLE_EMPRESA, Usuario.ROLE_ADMIN) //Alteração de produto
    //.antMatchers(HttpMethod.DELETE, "/api/produto/*").hasAnyAuthority(Usuario.ROLE_EMPRESA, Usuario.ROLE_ADMIN) //Exclusão de produto
    //.antMatchers(HttpMethod.GET, "/api/produto/").hasAnyAuthority(Usuario.ROLE_CLIENTE, Usuario.ROLE_EMPRESA, Usuario.ROLE_ADMIN) //Consulta de produto
    
    //.antMatchers(HttpMethod.GET, "/api/empresa").hasAnyAuthority(Usuario.ROLE_EMPRESA, Usuario.ROLE_CLIENTE, Usuario.ROLE_ADMIN) //Consulta de produto
    //.antMatchers(HttpMethod.PUT, "/api/empresa/*").hasAnyAuthority(Usuario.ROLE_EMPRESA, Usuario.ROLE_ADMIN) //Alteração de produto
    //.antMatchers(HttpMethod.DELETE, "/api/empresa/*").hasAnyAuthority(Usuario.ROLE_EMPRESA, Usuario.ROLE_ADMIN) //Exclusão de produto

    //.antMatchers(HttpMethod.POST, "/api/categoriaproduto").hasAnyAuthority(Usuario.ROLE_EMPRESA, Usuario.ROLE_ADMIN) //Cadastro de produto
    //.antMatchers(HttpMethod.PUT, "/api/categoriaproduto/*").hasAnyAuthority(Usuario.ROLE_EMPRESA, Usuario.ROLE_ADMIN) //Alteração de produto
    //.antMatchers(HttpMethod.DELETE, "/api/categoriaproduto/*").hasAnyAuthority(Usuario.ROLE_EMPRESA, Usuario.ROLE_ADMIN) //Exclusão de produto
    //.antMatchers(HttpMethod.GET, "/api/categoriaproduto/").hasAnyAuthority(Usuario.ROLE_CLIENTE, Usuario.ROLE_EMPRESA, Usuario.ROLE_ADMIN) //Consulta de produto
    
    //.antMatchers(HttpMethod.PUT, "/api/cliente/*").hasAnyAuthority(Usuario.ROLE_CLIENTE, Usuario.ROLE_ADMIN) //Alteração de produto
    //.antMatchers(HttpMethod.DELETE, "/api/cliente/*").hasAnyAuthority(Usuario.ROLE_CLIENTE, Usuario.ROLE_ADMIN) //Exclusão de produto
    //.antMatchers(HttpMethod.GET, "/api/cliente/").hasAnyAuthority(Usuario.ROLE_CLIENTE, Usuario.ROLE_EMPRESA, Usuario.ROLE_ADMIN) //Consulta de produto
    
    //.antMatchers(HttpMethod.POST, "/api/pedido").hasAnyAuthority(Usuario.ROLE_CLIENTE,Usuario.ROLE_EMPRESA, Usuario.ROLE_EMPRESA, Usuario.ROLE_ADMIN) //Cadastro de produto
    //.antMatchers(HttpMethod.PUT, "/api/pedido/*").hasAnyAuthority(Usuario.ROLE_CLIENTE,Usuario.ROLE_EMPRESA, Usuario.ROLE_EMPRESA, Usuario.ROLE_ADMIN) //Alteração de produto
    //.antMatchers(HttpMethod.DELETE, "/api/pedido/*").hasAnyAuthority(Usuario.ROLE_CLIENTE, Usuario.ROLE_EMPRESA, Usuario.ROLE_EMPRESA, Usuario.ROLE_ADMIN) //Exclusão de produto
    //.antMatchers(HttpMethod.GET, "/api/pedido/").hasAnyAuthority(Usuario.ROLE_CLIENTE, Usuario.ROLE_EMPRESA, Usuario.ROLE_ADMIN) //Consulta de produto
    

    .antMatchers(HttpMethod.POST, "/api/produto").permitAll()
    .antMatchers(HttpMethod.PUT, "/api/produto/*").permitAll()
    .antMatchers(HttpMethod.DELETE, "/api/produto/*").permitAll()
    .antMatchers(HttpMethod.GET, "/api/produto/").permitAll()
        
    .antMatchers(HttpMethod.GET, "/api/empresa").permitAll()
    .antMatchers(HttpMethod.PUT, "/api/empresa/*").permitAll()
    .antMatchers(HttpMethod.DELETE, "/api/empresa/*").permitAll()
    
    .antMatchers(HttpMethod.POST, "/api/categoriaproduto").permitAll()
    .antMatchers(HttpMethod.PUT, "/api/categoriaproduto/*").permitAll()
    .antMatchers(HttpMethod.DELETE, "/api/categoriaproduto/*").permitAll()
    .antMatchers(HttpMethod.GET, "/api/categoriaproduto/").permitAll()
        
    .antMatchers(HttpMethod.PUT, "/api/cliente/*").permitAll()
    .antMatchers(HttpMethod.DELETE, "/api/cliente/*").permitAll()
    .antMatchers(HttpMethod.GET, "/api/cliente/").permitAll()
        
    .antMatchers(HttpMethod.POST, "/api/pedido").permitAll()
    .antMatchers(HttpMethod.PUT, "/api/pedido/*").permitAll()
    .antMatchers(HttpMethod.DELETE, "/api/pedido/*").permitAll()
    .antMatchers(HttpMethod.GET, "/api/pedido/").permitAll();

	 	
       	//.anyRequest().hasAnyAuthority(Usuario.ROLE_CLIENTE, Usuario.ROLE_EMPRESA, Usuario.ROLE_ADMIN)
        //	.and().addFilterBefore(
        //		new JwtTokenAuthenticationFilter(jwtTokenProvider),
        //		UsernamePasswordAuthenticationFilter.class);
	 
    return http.build();
}


    @Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }

}