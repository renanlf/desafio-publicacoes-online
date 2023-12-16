package online.renanlf.sysproc;

import java.util.HashMap;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserE2ETest {
	
	@Test
	@Order(1)
	public void postUser(@Autowired WebTestClient webClient) throws Exception {
		var bodyMap = new HashMap<String, String>();
	    bodyMap.put("email","leandrorenanf@gmail.com");
	    bodyMap.put("name","Renan Fernandes");
		
	    // expect to not allow when password is not set
		webClient
			.post().uri("/users")
			.contentType(MediaType.APPLICATION_JSON)
			.body(BodyInserters.fromValue(bodyMap))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody().jsonPath("password").isEqualTo("A senha deve ser fornecida");

	    bodyMap.put("password","123456");
	    // after put the password it is expected to work properly.
		webClient
			.post().uri("/users")
			.contentType(MediaType.APPLICATION_JSON)
			.body(BodyInserters.fromValue(bodyMap))
			.exchange()
			.expectStatus().isOk()
		    .expectBody().jsonPath("email").isEqualTo("leandrorenanf@gmail.com");
		
		// expects to block if one tries to add the same e-mail
		webClient
			.post().uri("/users")
			.contentType(MediaType.APPLICATION_JSON)
			.body(BodyInserters.fromValue(bodyMap))
			.exchange()
			.expectStatus().isForbidden();
	}
	
}
