package com.example.api.service.impl;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.api.domain.Customer;
import com.example.api.repository.CustomerRepository;
import com.example.api.service.ICustomerService;

@Service
public class CustomerServiceImpl implements ICustomerService {

	@Autowired
	private CustomerRepository repository;

	@Override
	public Page<Customer> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public Customer findById(Long id) {
		Optional<Customer> optional = repository.findById(id);

		if (optional.isPresent()) {
			return optional.get();
		} else {
			throw new EntityNotFoundException();
		}
	}

	@Override
	public Customer save(Customer customer) {
		return repository.save(customer);
	}

	@Override
	public Customer update(Customer customer) {
		Optional.of(customer).map(c -> c.getId()).filter(id -> id > 0)
				.orElseThrow(() -> new IllegalStateException("Id should be a valid number."));

		Optional<Customer> optional = repository.findById(customer.getId());

		if (optional.isPresent()) {
			return repository.save(customer);
		} else {
			throw new EntityNotFoundException();
		}
	}

	@Override
	public void remove(Long id) {
		Optional<Customer> optional = repository.findById(id);

		if (optional.isPresent()) {
			repository.delete(optional.get());
		} else {
			throw new EntityNotFoundException();
		}
	}

}
