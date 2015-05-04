package com.devcrumb;

import java.util.Date;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.devcrumb.dao.EventDao;
import com.devcrumb.dao.PersonDao;
import com.devcrumb.model.Event;
import com.devcrumb.model.Person;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Standalone application with Spring Data JPA, Hibernate and Maven
 * 
 * @author DevCrumb.com
 */
public class App {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		
		EventDao eventDao = context.getBean(EventDao.class);
		PersonDao personDao = context.getBean(PersonDao.class);
		
		Event event1 = createEvent("Event 1", new Date(), "POINT(10 5)");
		Person person1 = new Person("John", "Doe");
		person1.setEvent(event1);
		event1.getPersons().add(person1);
		Long storedEventId = eventDao.save(event1).getId();
		
		System.out.println("Count Event records: " + eventDao.count());
		
		List<Event> events = (List<Event>) eventDao.findAll();
		for (Event event : events) {
			System.out.println(event);
		}
		/**
		 * TODO Lazy 
		 */
		Event storedEvent = eventDao.getById(storedEventId);
		System.out.println(storedEvent.getPersons().get(0));
		
		Person peter = new Person("Peter", "Sagan");
		Person nasta = new Person("Nasta", "Kuzminova");

		// Add new Person records
		personDao.save(peter);
		personDao.save(nasta);

		// Count Person records
		System.out.println("Count Person records: " + personDao.count());

		// Print all records
		List<Person> persons = (List<Person>) personDao.findAll();
		for (Person person : persons) {
			System.out.println(person);
		}

		// Find Person by surname
		System.out.println("Find by surname 'Sagan': "	+ personDao.findBySurname("Sagan"));

		// Update Person
		nasta.setName("Barbora");
		nasta.setSurname("Spotakova");
		personDao.save(nasta);

		System.out.println("Find by id 2: " + personDao.findOne(2L));

		// Remove record from Person
		personDao.delete(2L);

		// And finally count records
		System.out.println("Count Person records: " + personDao.count());

		context.close();
	}
	
	private static Event createEvent(String title, Date theDate, String wktPoint) {
        Geometry geom = wktToGeometry(wktPoint);

        if (!geom.getGeometryType().equals("Point")) {
            throw new RuntimeException("Geometry must be a point. Got a " + geom.getGeometryType());
        }


        Event theEvent = new Event();
        theEvent.setTitle(title);
        theEvent.setDate(theDate);
        theEvent.setLocation((Point) geom);
        return theEvent;
    }
	
	private static Geometry wktToGeometry(String wktPoint) {
        WKTReader fromText = new WKTReader();
        Geometry geom = null;
        try {
            geom = fromText.read(wktPoint);
        } catch (ParseException e) {
            throw new RuntimeException("Not a WKT string:" + wktPoint);
        }
        return geom;
    }
}
