package com.luv2code.ecommerce.entity.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luv2code.ecommerce.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
	
	// query method to return customer with the given email
	Customer findByEmail(String theEmail);

}
