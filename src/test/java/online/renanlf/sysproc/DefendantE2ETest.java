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
public class DefendantE2ETest {

	private static String token;

	@BeforeAll
	public static void createToken(@Autowired WebTestClient webClient) {
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
	}

	@Test
	@Order(1)
	public void postDefendant(@Autowired WebTestClient webClient) throws Exception {
		var bodyMap = new HashMap<String, String>();
		bodyMap.put("cpfCnpj", "123456");

		webClient.post().uri("/defendants").header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(bodyMap)).exchange()
				.expectStatus().isBadRequest().expectBody().jsonPath("name")
				.isEqualTo("Nome do réu é obrigatório");

		bodyMap.put("name", "Defendant Name 1");

		webClient.post().uri("/defendants").header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(bodyMap)).exchange()
				.expectStatus().isOk().expectBody().jsonPath("cpfCnpj").isEqualTo("123456");

	}

	@Test
	@Order(2)
	public void getAllDefendants(@Autowired WebTestClient webClient) {
		webClient.get().uri("/defendants").header("Authorization", "Bearer " + token)
			.exchange().expectStatus().isOk()
			.expectBody()
				.jsonPath("$").isArray();
	}
	
	@Test
	@Order(3)
	public void deleteDefendantByCpfCnpj(@Autowired WebTestClient webClient) {
		webClient.get().uri("/defendants/123456")
			.header("Authorization", "Bearer " + token)
			.exchange()
			.expectBody().jsonPath("name").isEqualTo("Defendant Name 1");
		
		webClient.delete().uri("/defendants/123456")
			.header("Authorization", "Bearer " + token)
			.exchange()
			.expectStatus().isOk();
		
		webClient.get().uri("/defendants/123456")
			.header("Authorization", "Bearer " + token)
			.exchange()
			.expectStatus().isForbidden()
			.expectBody().isEmpty();
	
	}

}
