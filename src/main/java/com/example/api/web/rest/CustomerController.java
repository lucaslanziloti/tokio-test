package com.example.api.web.rest;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.domain.Customer;
import com.example.api.service.ICustomerService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	private ICustomerService customerService;

	@GetMapping
	@ApiOperation(value = "Search customers using Pageable")
	public Page<Customer> findAll(
			@PageableDefault(sort = "name", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
		return customerService.findAll(pageable);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Search customer by id")
	public ResponseEntity<Customer> findById(@PathVariable Long id) {
		try {
			Customer placeDto = customerService.findById(id);

			return ResponseEntity.ok(placeDto);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	@ApiOperation(value = "Save a customer")
	public ResponseEntity<Customer> save(@RequestBody @Valid Customer customer) {
		customer = customerService.save(customer);

		return ResponseEntity.status(HttpStatus.CREATED).body(customer);
	}

	@PutMapping
	@ApiOperation(value = "Update a customer")
	public ResponseEntity<Customer> update(@RequestBody @Valid Customer customer) {
		try {
			customer = customerService.update(customer);

			return ResponseEntity.ok(customer);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "Remove a customer")
	public ResponseEntity<?> remover(@PathVariable Long id) {
		try {
			customerService.remove(id);

			return ResponseEntity.ok().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

}
