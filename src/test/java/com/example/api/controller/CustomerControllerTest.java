package com.example.api.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.api.ApiApplication;
import com.example.api.domain.Customer;
import com.example.api.service.ICustomerService;
import com.example.api.web.rest.CustomerController;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
@ContextConfiguration(classes={ApiApplication.class})
public class CustomerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ICustomerService customerService;

	private static final String CONTENT_LIST = "$.content";
	private static final String ENDPOINT = "/customers";
	
	@Before
	public void setup() {
        MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldReturnOnePlaceById() throws Exception {
		Customer customer = createPlaceList().get(0);
		
		when(customerService.findById(1L)).thenReturn(customer);
		
		this.mockMvc.perform(get(ENDPOINT + "/1"))
				.andDo(print())
				.andExpect(status().isOk());
		
		Mockito.verify(customerService, Mockito.times(1)).findById(1L);
	}

	@Test
	public void shouldReturnNotFoundOnFindById() throws Exception {
		when(customerService.findById(1L)).thenThrow(EntityNotFoundException.class);
		
		this.mockMvc.perform(get(ENDPOINT + "/1"))
				.andDo(print())
				.andExpect(status().isNotFound());
		
		Mockito.verify(customerService, Mockito.times(1)).findById(1L);
	}

	@Test
	public void shouldReturnAllItems() throws Exception {
		when(customerService.findAll(Mockito.any(Pageable.class))).thenReturn(new PageImpl<>(createPlaceList()));

		this.mockMvc.perform(get(ENDPOINT)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath(CONTENT_LIST).isArray())
				.andExpect(jsonPath(CONTENT_LIST).isNotEmpty())
				.andExpect(jsonPath(CONTENT_LIST, hasSize(createPlaceList().size())));
	}

	@Test
	public void shouldSavePlace() throws Exception {
		Customer customer = createPlaceList().get(0);
		
		when(customerService.save(Mockito.any(Customer.class))).thenReturn(customer);
		
		this.mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertObjectToJson(customer)))
				.andDo(print())
				.andExpect(status().isCreated());
		
		Mockito.verify(customerService, Mockito.times(1)).save(customer);
	}

	@Test
	public void shouldUpdatePlace() throws Exception {
		Customer customer = createPlaceList().get(0);
		
		when(customerService.update(Mockito.any(Customer.class))).thenReturn(customer);
		
		this.mockMvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertObjectToJson(customer)))
				.andDo(print())
				.andExpect(status().isOk());
		
		Mockito.verify(customerService, Mockito.times(1)).update(customer);
	}

	@Test
	public void shouldDeletePlace() throws Exception {
		doNothing().when(customerService).remove(Mockito.any(Long.class));
		
		this.mockMvc.perform(delete(ENDPOINT + "/1"))
				.andDo(print())
				.andExpect(status().isOk());
		
		Mockito.verify(customerService, Mockito.times(1)).remove(1L);
	}

	@Test
	public void shouldThrowNotFoundOnUpdate() throws Exception {
		Customer customer = createPlaceList().get(0);
		
		when(customerService.update(customer)).thenThrow(EntityNotFoundException.class);
		
		this.mockMvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertObjectToJson(customer)))
				.andDo(print())
				.andExpect(status().isNotFound());
		
		Mockito.verify(customerService, Mockito.times(1)).update(customer);
	}

	@Test
	public void shouldThrowNotFoundOnDelete() throws Exception {
		doThrow(EntityNotFoundException.class).when(customerService).remove(Mockito.any(Long.class));
		
		this.mockMvc.perform(delete(ENDPOINT + "/1"))
				.andDo(print())
				.andExpect(status().isNotFound());
		
		Mockito.verify(customerService, Mockito.times(1)).remove(1L);
	}

	private List<Customer> createPlaceList() {
		List<Customer> listPopulated = new ArrayList<>();

		listPopulated.add(new Customer(1L, "Teste 1", "teste_1@mail.com"));
		listPopulated.add(new Customer(2L, "Teste 2", "teste_2@mail.com"));
		listPopulated.add(new Customer(3L, "Teste 3", "teste_3@mail.com"));
		listPopulated.add(new Customer(4L, "Teste 4", "teste_4@mail.com"));

		return listPopulated;
	}
	
	public String convertObjectToJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(object);
    }
}