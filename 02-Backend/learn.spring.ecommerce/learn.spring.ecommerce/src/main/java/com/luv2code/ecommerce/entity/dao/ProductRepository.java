package com.luv2code.ecommerce.entity.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.luv2code.ecommerce.entity.Product;

@RepositoryRestResource
public interface ProductRepository extends JpaRepository<Product, Long> {

	Page<Product> findByCategoryId(@Param("id") Long id, Pageable pageable);
	// select * from Product where category_id=?
	
	Page<Product> findByNameContaining(@Param("name") String name,Pageable page);
	
	// select * from Product p where p.name like CONCAT('%' :name '%');

}
