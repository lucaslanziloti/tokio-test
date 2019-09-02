package com.example.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.api.dto.CustomerDto;

public interface ICustomerService {

	public CustomerDto findById(Long id);

	public CustomerDto save(CustomerDto customerDto);

	public CustomerDto update(CustomerDto customerDto);

	public void remove(Long id);

	public Page<CustomerDto> findAll(Pageable pageable);
}
