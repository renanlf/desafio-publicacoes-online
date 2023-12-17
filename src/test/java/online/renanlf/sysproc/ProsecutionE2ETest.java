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
public class ProsecutionE2ETest {

	private static String token;

	@BeforeAll
	public static void createToken(@Autowired WebTestClient webClient) {
		var bodyMap = new HashMap<String, String>();
		bodyMap.put("email", "prosecutionTest@gmail.com");
		bodyMap.put("name", "Renan Fernandes");
		bodyMap.put("password", "123456");

		webClient.post().uri("/users").contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(bodyMap))
				.exchange().expectStatus().isOk().expectBody().jsonPath("email").isEqualTo("prosecutionTest@gmail.com");

		Mono<LoginRequisition> mono = Mono
				.just(LoginRequisition.builder().email("prosecutionTest@gmail.com").password("123456").build());

		token = webClient.post().uri("/login").contentType(MediaType.APPLICATION_JSON)
				.body(mono, LoginRequisition.class).exchange().expectBody(String.class).returnResult()
				.getResponseBody();
	}

	@Test
	@Order(1)
	public void postProsecution(@Autowired WebTestClient webClient) throws Exception {
		var bodyMap = new HashMap<String, String>();
		bodyMap.put("protocol", "123456");
		bodyMap.put("description", "This is a prosecution");

		webClient.post().uri("/prosecutions").header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(bodyMap)).exchange()
				.expectStatus().isBadRequest().expectBody().jsonPath("openingDate")
				.isEqualTo("Data de abertura do processo não pode ser vazia");

		bodyMap.put("openingDate", "2003-01-12");

		webClient.post().uri("/prosecutions").header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(bodyMap)).exchange()
				.expectStatus().isOk().expectBody().jsonPath("protocol").isEqualTo("123456");

	}

	@Test
	@Order(2)
	public void getAllProsecution(@Autowired WebTestClient webClient) {
		webClient.get().uri("/prosecutions").header("Authorization", "Bearer " + token)
			.exchange().expectStatus().isOk()
			.expectBody()
				.jsonPath("$").isArray()
				.jsonPath("$[0].protocol").isEqualTo("123456")
				.jsonPath("$[1]").doesNotExist();
	}
	
	@Test
	@Order(3)
	public void getProsecutionByProtocol(@Autowired WebTestClient webClient) {
		webClient.get().uri("/prosecutions/123456").header("Authorization", "Bearer " + token)
			.exchange().expectStatus().isOk()
			.expectBody()
				.jsonPath("$.protocol").isEqualTo("123456")
				.jsonPath("$.description").isEqualTo("This is a prosecution");
	}
	
	@Test
	@Order(4)
	public void getProsecutionLikeDescription(@Autowired WebTestClient webClient) {
		//adding new prosecutions	
		var bodyMap = new HashMap<String, String>();
		bodyMap.put("protocol", "1234567");
		bodyMap.put("description", "This is a prosecution");
		bodyMap.put("openingDate", "2021-05-05");
		
		webClient.post().uri("/prosecutions").header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(bodyMap)).exchange()
			.expectStatus().isOk();
		
		bodyMap = new HashMap<String, String>();
		bodyMap.put("protocol", "1234");
		bodyMap.put("description", "This is another prosecution");
		bodyMap.put("openingDate", "2019-07-18");
		
		webClient.post().uri("/prosecutions").header("Authorization", "Bearer " + token)
		.contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(bodyMap)).exchange()
		.expectStatus().isOk();
		
		bodyMap = new HashMap<String, String>();
		bodyMap.put("protocol", "123");
		bodyMap.put("description", "Well, this is another one");
		bodyMap.put("openingDate", "2012-12-12");
		
		webClient.post().uri("/prosecutions").header("Authorization", "Bearer " + token)
		.contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(bodyMap)).exchange()
		.expectStatus().isOk();
		
		// getting them by description
		webClient.get().uri("/prosecutions?description=another")
			.header("Authorization", "Bearer " + token)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
				.jsonPath("$").isArray()
				.jsonPath("$[0].protocol").isEqualTo("1234")
				.jsonPath("$[0].description").isEqualTo("This is another prosecution")
				.jsonPath("$[1].protocol").isEqualTo("123")
				.jsonPath("$[1].description").isEqualTo("Well, this is another one")
				.jsonPath("$[2]").doesNotExist();
	}
	
	@Test
	@Order(5)
	public void deleteProsecutionByProtocol(@Autowired WebTestClient webClient) {
		webClient.get().uri("/prosecutions/1234567")
			.header("Authorization", "Bearer " + token)
			.exchange()
			.expectBody().jsonPath("description").isEqualTo("This is a prosecution");
		
		webClient.delete().uri("/prosecutions/1234567")
			.header("Authorization", "Bearer " + token)
			.exchange()
			.expectStatus().isOk();
		
		webClient.get().uri("/prosecutions/1234567")
			.header("Authorization", "Bearer " + token)
			.exchange()
			.expectStatus().isForbidden()
			.expectBody().isEmpty();
	
	}
	
	@Test
	@Order(6)
	public void postDuplicateProsecutionProtocol(@Autowired WebTestClient webClient) {
		var bodyMap = new HashMap<String, String>();
		bodyMap.put("protocol", "123456");
		bodyMap.put("description", "This is a prosecution duplicated");
		bodyMap.put("openingDate", "2015-01-12");

		webClient.post().uri("/prosecutions").header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(bodyMap)).exchange()
				.expectStatus().isEqualTo(409)
				.expectBody().jsonPath("protocol").isEqualTo("Protocolo 123456 já existe!");

	}

}
