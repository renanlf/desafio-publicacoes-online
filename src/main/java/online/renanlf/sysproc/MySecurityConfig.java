package online.renanlf.sysproc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import online.renanlf.sysproc.filters.JwtAuthenticationFilter;

/**
 * This class is heavely inspired by https://www.baeldung.com/spring-security-login
 * @author renanlf
 *
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class MySecurityConfig {
	 
	private final AuthenticationProvider authProvider;
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
        		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        		.and().authenticationProvider(authProvider)
            	.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                
            	.authorizeHttpRequests()
            		.requestMatchers(HttpMethod.POST, "users").permitAll()
            		.requestMatchers(HttpMethod.POST, "login").permitAll()
                
                	// USER permissions
                	.requestMatchers("prosecution/**").hasAuthority("USER")
                	
                	.anyRequest().authenticated() 
                	
                .and().csrf().disable().build();
	}
	
}
