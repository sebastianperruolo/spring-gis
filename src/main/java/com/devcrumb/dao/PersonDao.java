package com.devcrumb.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.devcrumb.model.Person;

/**
 * Person dao interface
 * 
 * @author DevCrumb.com
 */
@Repository
public interface PersonDao extends CrudRepository<Person, Long> {
	
	public List<Person> findBySurname(String surname);
}
