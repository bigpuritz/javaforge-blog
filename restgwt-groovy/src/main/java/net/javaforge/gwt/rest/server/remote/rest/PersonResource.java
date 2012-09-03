package net.javaforge.gwt.rest.server.remote.rest;

import net.javaforge.gwt.rest.shared.domain.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/person")
public class PersonResource {

    private static final Logger logger = LoggerFactory
            .getLogger(PersonResource.class);

    private static int counter = 1;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> getPersons() {

        List<Person> persons = new ArrayList<Person>();
        for (int i = 0; i < 5; i++) {
            persons.add(new Person(counter, "name-" + counter, new Date()));
            counter++;
        }

        logger.info("Returning {} persons...", persons.size());

        return persons;
    }

    @GET
    @Path("book/{id}")
    public void doPersons(@PathParam("id") String isbn) {

    }

}
