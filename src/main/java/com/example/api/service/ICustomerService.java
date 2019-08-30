package com.example.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.api.domain.Customer;

public interface ICustomerService {

	public Customer findById(Long id);

	public Customer save(Customer customer);

	public Customer update(Customer customer);

	public void remove(Long id);

	public Page<Customer> findAll(Pageable pageable);
}
