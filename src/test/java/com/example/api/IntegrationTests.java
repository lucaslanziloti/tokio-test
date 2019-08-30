package com.example.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.api.domain.Customer;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IntegrationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private StringBuilder endpoint = new StringBuilder();
	
	@Before
	public void setup() {
		endpoint.append("http://localhost:").append(port).append("/customers");
	}

	@Test
	public void aShouldFindAPlaceById() {
		endpoint.append("/1");

		ResponseEntity<Customer> result = this.restTemplate.getForEntity(endpoint.toString(), Customer.class);

		assertEquals(200, result.getStatusCodeValue());
		assertEquals(result.getBody().getId().intValue(), 1);
	}

	@Test
	public void bShouldDeletePlace() {
		endpoint.append("/1");
		restTemplate.delete(endpoint.toString());

		ResponseEntity<Customer> result = this.restTemplate.getForEntity(endpoint.toString(), Customer.class);

		assertEquals(404, result.getStatusCodeValue());
	}

	@Test
	public void shouldSaveAPlace() throws URISyntaxException {
		Customer customer = new Customer();
		customer.setEmail("test@mail.com");
		customer.setName("Name integration test");

		HttpHeaders headers = new HttpHeaders();

		HttpEntity<Customer> request = new HttpEntity<>(customer, headers);

		ResponseEntity<Customer> result = this.restTemplate.postForEntity(new URI(endpoint.toString()), request,
				Customer.class);

		assertEquals(201, result.getStatusCodeValue());
		assertNotNull(result.getBody().getId());

		assertEquals(result.getBody().getEmail(), customer.getEmail());
		assertEquals(result.getBody().getName(), customer.getName());
	}

	@Test
	public void shouldUpdateAPlace() {
		Customer customer = new Customer();
		customer.setId(2L);
		customer.setEmail("test_updated@mail.com");
		customer.setName("Name integration test updated");

		HttpHeaders headers = new HttpHeaders();

		HttpEntity<Customer> request = new HttpEntity<>(customer, headers);

		ResponseEntity<Customer> result = this.restTemplate.exchange(endpoint.toString(), HttpMethod.PUT, request,
				Customer.class);

		assertEquals(200, result.getStatusCodeValue());

		assertEquals(result.getBody().getId().intValue(), 2);
		assertEquals(result.getBody().getEmail(), customer.getEmail());
		assertEquals(result.getBody().getName(), customer.getName());
	}

}
