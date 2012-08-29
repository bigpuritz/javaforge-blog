package net.javaforge.gwt.rest.client.remote.rest;

import com.google.gwt.core.client.GWT;
import net.javaforge.gwt.rest.shared.domain.Person;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestService;
import org.fusesource.restygwt.client.RestServiceProxy;

import javax.ws.rs.GET;
import java.util.List;

public interface PersonResourceAsync extends RestService {

    @GET
    void getPersons(MethodCallback<List<Person>> callback);

    /**
     * Utility class to get the instance of the Rest Service
     */
    public static final class Util {

        private static PersonResourceAsync instance;

        public static final PersonResourceAsync get() {
            if (instance == null) {
                instance = GWT.create(PersonResourceAsync.class);
                ((RestServiceProxy) instance).setResource(new Resource(
                        "rest/person"));
            }
            return instance;
        }

        private Util() {
            // Utility class should not be instantiated
        }
    }

}
