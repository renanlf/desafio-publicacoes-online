package online.renanlf.sysproc.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import online.renanlf.sysproc.model.LoginRequisition;
import online.renanlf.sysproc.model.User;

@Service
public class JwtService {
	
	private final String SECRET_KEY = "SECRET_KEY";
	private final int HOURS_UNTIL_EXPIRATION = 24;

	public Optional<String> getUsername(String token) {
		return Optional.ofNullable(JWT.decode(token).getSubject());
	}
	
	private Optional<Date> getExpirationDate(String token) {
		return Optional.ofNullable(JWT.decode(token).getExpiresAt());
	}
	
	public String generateToken(LoginRequisition loginReq) {
		var timeNow = System.currentTimeMillis();
		
		return JWT.create()
				.withSubject(loginReq.getEmail())
				.withIssuedAt(new Date(timeNow))
				.withExpiresAt(new Date(timeNow + (1000 * 60 * HOURS_UNTIL_EXPIRATION)))
				.sign(Algorithm.HMAC512(SECRET_KEY));
	}
	
	public boolean isExpired(String token) {
		var now = new Date();
		return getExpirationDate(token)
					.orElse(now)
				.before(now);
	}
	
	public boolean isValid(String token, User user) {
		try {
			JWT.require(Algorithm.HMAC256(SECRET_KEY))
				.withSubject(user.getEmail())
				.build()
				.verify(token);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

}
