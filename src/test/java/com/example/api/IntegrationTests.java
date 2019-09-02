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

import com.example.api.dto.CustomerDto;


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
	public void aShouldFindACustomerById() {
		endpoint.append("/1");

		ResponseEntity<CustomerDto> result = this.restTemplate.getForEntity(endpoint.toString(), CustomerDto.class);

		assertEquals(200, result.getStatusCodeValue());
		assertEquals(result.getBody().getId().intValue(), 1);
	}

	@Test
	public void bShouldDeleteCustomer() {
		endpoint.append("/1");
		restTemplate.delete(endpoint.toString());

		ResponseEntity<CustomerDto> result = this.restTemplate.getForEntity(endpoint.toString(), CustomerDto.class);

		assertEquals(404, result.getStatusCodeValue());
	}

	@Test
	public void shouldSaveACustomer() throws URISyntaxException {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setEmail("test@mail.com");
		customerDto.setName("Name integration test");

		HttpHeaders headers = new HttpHeaders();

		HttpEntity<CustomerDto> request = new HttpEntity<>(customerDto, headers);

		ResponseEntity<CustomerDto> result = this.restTemplate.postForEntity(new URI(endpoint.toString()), request,
				CustomerDto.class);

		assertEquals(201, result.getStatusCodeValue());
		assertNotNull(result.getBody().getId());

		assertEquals(result.getBody().getEmail(), customerDto.getEmail());
		assertEquals(result.getBody().getName(), customerDto.getName());
	}

	@Test
	public void shouldUpdateACustomer() {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setId(2L);
		customerDto.setEmail("test_updated@mail.com");
		customerDto.setName("Name integration test updated");

		HttpHeaders headers = new HttpHeaders();

		HttpEntity<CustomerDto> request = new HttpEntity<>(customerDto, headers);

		ResponseEntity<CustomerDto> result = this.restTemplate.exchange(endpoint.toString(), HttpMethod.PUT, request,
				CustomerDto.class);

		assertEquals(200, result.getStatusCodeValue());

		assertEquals(result.getBody().getId().intValue(), 2);
		assertEquals(result.getBody().getEmail(), customerDto.getEmail());
		assertEquals(result.getBody().getName(), customerDto.getName());
	}

}
