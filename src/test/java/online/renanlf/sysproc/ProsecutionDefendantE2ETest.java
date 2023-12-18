package online.renanlf.sysproc;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeAll;
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
public class ProsecutionDefendantE2ETest {

	private static String token;

	@BeforeAll
	public static void createTokenAndData(@Autowired WebTestClient webClient) {
		var bodyMap = new HashMap<String, String>();
		bodyMap.put("email", "defendantTests@gmail.com");
		bodyMap.put("name", "Renan Fernandes");
		bodyMap.put("password", "123456");

		webClient.post().uri("/users").contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(bodyMap))
				.exchange().expectStatus().isOk().expectBody().jsonPath("email").isEqualTo("defendantTests@gmail.com");

		Mono<LoginRequisition> mono = Mono
				.just(LoginRequisition.builder().email("defendantTests@gmail.com").password("123456").build());

		token = webClient.post().uri("/login").contentType(MediaType.APPLICATION_JSON)
				.body(mono, LoginRequisition.class).exchange().expectBody(String.class).returnResult()
				.getResponseBody();
		
		// creates a prosecution and a defendant
		
		bodyMap = new HashMap<String, String>();
		bodyMap.put("protocol", "12345678");
		bodyMap.put("description", "This is a prosecution");
		bodyMap.put("openingDate", "2015-01-12");

		webClient.post().uri("/prosecutions").header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(bodyMap)).exchange()
				.expectStatus().isOk();
		
		
		bodyMap = new HashMap<String, String>();
		bodyMap.put("cpfCnpj", "123456789");
		bodyMap.put("name", "Defendant Name 2");
		
		webClient.post().uri("/defendants").header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(bodyMap)).exchange()
			.expectStatus().isOk();

	}

	@Test
	@Order(1)
	public void postProsecutionDefendant(@Autowired WebTestClient webClient) throws Exception {
		webClient.put().uri("/prosecutions/12345678/defendants/123456").header("Authorization", "Bearer " + token)
			.exchange()
			.expectStatus().isNotFound();
		
		webClient.put().uri("/prosecutions/12345/defendants/123456").header("Authorization", "Bearer " + token)
			.exchange()
			.expectStatus().isForbidden();
		
		webClient.put().uri("/prosecutions/12345678/defendants/123456789").header("Authorization", "Bearer " + token)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
				.jsonPath("defendants[0].name").isEqualTo("Defendant Name 2");

	}

	@Test
	@Order(2)
	public void getAllDefendantsForProsecution(@Autowired WebTestClient webClient) {
		webClient.get().uri("/prosecutions/12345678/defendants").header("Authorization", "Bearer " + token)
			.exchange().expectStatus().isOk()
			.expectBody()
				.jsonPath("$").isArray()
				.jsonPath("$[0].name").isEqualTo("Defendant Name 2")
				.jsonPath("$[1]").doesNotExist();
	}

}
