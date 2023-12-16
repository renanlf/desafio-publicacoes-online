package online.renanlf.sysproc.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import online.renanlf.sysproc.repository.UserRepository;
import online.renanlf.sysproc.service.JwtService;

/**
 * This class is heavely based on Amigoscode's implementation YouTube video
 * https://www.youtube.com/watch?v=KxqlJblhzfI 
 * @author renanlf
 *
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		var bearerToken = request.getHeader("Authorization");
		
		if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
			var token = bearerToken.substring(7);
			
			if(SecurityContextHolder.getContext().getAuthentication() == null) {
				
				var user = userRepo.findByToken(token)
						.orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Token not found"));
				
				if(!jwtService.isExpired(token)) {				
					var authToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), user.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					
					SecurityContextHolder.getContext().setAuthentication(authToken);
				} else {
					// if it is expired then clear the token in db
					user.setToken(null);
					userRepo.save(user);
				}
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
