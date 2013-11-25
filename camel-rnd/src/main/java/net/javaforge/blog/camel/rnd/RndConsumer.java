package net.javaforge.blog.camel.rnd;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;
import org.apache.camel.util.ObjectHelper;

/**
 * The Rnd consumer.
 */
public class RndConsumer extends ScheduledPollConsumer {

    private final RndEndpoint endpoint;

    public RndConsumer(RndEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }

    @Override
    protected int poll() throws Exception {

        Exchange exchange = endpoint.createExchange();

        // create a message body
        exchange.getIn().setBody(generateRandomSequence());

        try {
            // send message to next processor in the route
            getProcessor().process(exchange);
            return 1; // number of messages polled
        } finally {
            // log exception if an exception occurred and was not handled
            if (exchange.getException() != null) {
                getExceptionHandler().handleException(
                        "Error processing exchange", exchange,
                        exchange.getException());
            }
        }
    }

    private String generateRandomSequence() {
        RndGenerator generator = endpoint.getGenerator();
        ObjectHelper.notNull(generator, "generator");
        return generator.length(endpoint.getLength())
                .chars(endpoint.getChars()).letters(endpoint.isLetters())
                .numbers(endpoint.isNumbers()).start(endpoint.getStart()).end(
                        endpoint.getEnd()).generate();
    }

}
