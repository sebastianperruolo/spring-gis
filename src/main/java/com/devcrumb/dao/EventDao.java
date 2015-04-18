package com.devcrumb.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.devcrumb.model.Event;

/**
 * Person dao interface
 * 
 * @author DevCrumb.com
 */
@Repository
public interface EventDao extends CrudRepository<Event, Long> {
	
}
