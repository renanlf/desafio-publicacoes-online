package online.renanlf.sysproc;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

import online.renanlf.sysproc.model.LoginRequisition;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginE2ETest {

	@Test
	@Order(1)
	public void postLogin(@Autowired WebTestClient webClient) throws Exception {
		var bodyMap = new HashMap<String, String>();
		bodyMap.put("email", "leandrorenanf@gmail.com");
		bodyMap.put("name", "Renan Fernandes");
		bodyMap.put("password", "123456");

		webClient.post().uri("/users").contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(bodyMap))
				.exchange()
				.expectStatus().isOk()
				.expectBody().jsonPath("email")
					.isEqualTo("leandrorenanf@gmail.com");

		var mono = Mono.just(LoginRequisition.builder().email("leandrorenanf@gmail.com").password("123456").build());

		String authToken = webClient.post().uri("/login").contentType(MediaType.APPLICATION_JSON)
				.body(mono, LoginRequisition.class).exchange().expectBody(String.class).returnResult()
				.getResponseBody();

		assertTrue(!authToken.isEmpty());
	}

}
