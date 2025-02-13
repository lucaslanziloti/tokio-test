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

import com.example.api.dto.CustomerDto;
import com.example.api.service.ICustomerService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	private ICustomerService customerService;

	@GetMapping
	@ApiOperation(value = "Search customers using Pageable")
	public Page<CustomerDto> findAll(
			@PageableDefault(sort = "name", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
		return customerService.findAll(pageable);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Search customer by id")
	public ResponseEntity<CustomerDto> findById(@PathVariable Long id) {
		try {
			CustomerDto customerDto = customerService.findById(id);

			return ResponseEntity.ok(customerDto);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	@ApiOperation(value = "Save a customer")
	public ResponseEntity<CustomerDto> save(@RequestBody @Valid CustomerDto customerDto) {
		customerDto = customerService.save(customerDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(customerDto);
	}

	@PutMapping
	@ApiOperation(value = "Update a customer")
	public ResponseEntity<CustomerDto> update(@RequestBody @Valid CustomerDto customerDto) {
		try {
			customerDto = customerService.update(customerDto);

			return ResponseEntity.ok(customerDto);
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
