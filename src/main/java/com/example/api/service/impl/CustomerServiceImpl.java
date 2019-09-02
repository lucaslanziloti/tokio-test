package com.example.api.service.impl;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.api.domain.Customer;
import com.example.api.dto.CustomerDto;
import com.example.api.repository.CustomerRepository;
import com.example.api.service.ICustomerService;

@Service
public class CustomerServiceImpl implements ICustomerService {

	@Autowired
	private CustomerRepository repository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Page<CustomerDto> findAll(Pageable pageable) {
		Page<Customer> list = repository.findAll(pageable);
		
		return list.map(this::convertToDTO);
	}

	@Override
	public CustomerDto findById(Long id) {
		Optional<Customer> optional = repository.findById(id);

		if (optional.isPresent()) {
			return convertToDTO(optional.get());
		} else {
			throw new EntityNotFoundException();
		}
	}

	@Override
	public CustomerDto save(CustomerDto customerDto) {
		Customer customer = repository.save(convertToEntity(customerDto));
		
		return convertToDTO(customer);
	}

	@Override
	public CustomerDto update(CustomerDto customerDto) {
		Optional.of(customerDto).map(c -> c.getId()).filter(id -> id > 0)
				.orElseThrow(() -> new IllegalStateException("Id should be a valid number."));

		Optional<Customer> optional = repository.findById(customerDto.getId());

		if (optional.isPresent()) {
			Customer customer = repository.save(convertToEntity(customerDto));
			
			return convertToDTO(customer);
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
	
	private CustomerDto convertToDTO(Customer customer) {
		return modelMapper.map(customer, CustomerDto.class);
	}

	private Customer convertToEntity(CustomerDto customerDto) {
		return modelMapper.map(customerDto, Customer.class);
	}

}
