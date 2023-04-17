package hr.algebra.toyswap.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final UserDetailsServiceImpl userDetailsServiceImpl;

  //  @Bean
  //  public AuthenticationManager authenticationManager(
  //      AuthenticationConfiguration authenticationConfiguration) throws Exception {
  //    return authenticationConfiguration.getAuthenticationManager();
  //  }
  //
  //  @Bean
  //  public DaoAuthenticationProvider authenticationProvider() {
  //    final var authProvider = new DaoAuthenticationProvider();
  //    authProvider.setUserDetailsService(userDetailsServiceImpl);
  //    authProvider.setPasswordEncoder(passwordEncoder());
  //    return authProvider;
  //  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //    http.cors()
    //        .and()
    //        .csrf()
    //        .disable()
    //        .sessionManagement()
    //        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    //        .and()
    //        .authorizeHttpRequests()
    //        .requestMatchers("/api/auth/**", "/", "/images/**", "/css/**")
    //        .permitAll()
    //        .anyRequest()
    //        .authenticated()
    //        .and()
    //        .formLogin()
    //        .loginPage("login")
    //        .defaultSuccessUrl("/")
    //        .and()
    //        .logout()
    //        .permitAll();
    //    http.authenticationProvider(authenticationProvider());
    //    http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
    http.authorizeHttpRequests()
        .requestMatchers("/", "/home", "/login", "/logout", "/register", "/images/**", "/post/**")
        .permitAll()
        .requestMatchers("/admin/**")
        .hasAuthority("ADMIN")
        .anyRequest()
        .authenticated()
        .and()
        .formLogin()
        .loginPage("/login")
        .defaultSuccessUrl("/home")
        .permitAll()
        .and()
        .logout()
        .logoutSuccessUrl("/home")
        .permitAll();

    return http.build();
  }
}
