package com.luv2code.ecommerce.entity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.luv2code.ecommerce.entity.State;


@RepositoryRestResource
public interface StateRepository extends JpaRepository<State, Integer> {
	
	List<State> findStateByCountryCode(@Param("code") String code);

}
