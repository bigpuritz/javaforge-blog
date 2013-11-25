package net.javaforge.blog.camel.rnd;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

import java.util.Map;

/**
 * Represents the component that manages {@link RndEndpoint}.
 */
public class RndComponent extends DefaultComponent {

    protected Endpoint createEndpoint(String uri, String remaining,
                                      Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new RndEndpoint(uri, this);

        // convert "generator" parameter to uppercase if exists
        String generator = (String) parameters.get("generator");
        if (generator != null)
            parameters.put("generator", generator.toUpperCase());

        setProperties(endpoint, parameters);
        return endpoint;
    }
}
