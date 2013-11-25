package net.javaforge.blog.camel.rnd;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.DefaultPollingEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;

/**
 * Represents a Rnd endpoint.
 */
@UriEndpoint(scheme = "rnd")
public class RndEndpoint extends DefaultPollingEndpoint {

    @UriParam
    private RndGenerator generator = RndGenerator.RANDOM;

    @UriParam
    private int length = 10;

    @UriParam
    private String chars = null;

    @UriParam
    private boolean letters = false;

    @UriParam
    private boolean numbers = false;

    @UriParam
    private int start = 0;

    @UriParam
    private int end = 0;

    public RndEndpoint() {
    }

    public RndEndpoint(String uri, RndComponent component) {
        super(uri, component);
    }

    public Producer createProducer() throws Exception {
        throw new RuntimeCamelException("Cannot produce to a RndEndpoint: "
                + getEndpointUri());
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        RndConsumer consumer = new RndConsumer(this, processor);
        configureConsumer(consumer);
        return consumer;
    }

    public boolean isSingleton() {
        return true;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public RndGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(RndGenerator generator) {
        this.generator = generator;
    }

    public String getChars() {
        return chars;
    }

    public void setChars(String chars) {
        this.chars = chars;
    }

    public boolean isLetters() {
        return letters;
    }

    public void setLetters(boolean letters) {
        this.letters = letters;
    }

    public boolean isNumbers() {
        return numbers;
    }

    public void setNumbers(boolean numbers) {
        this.numbers = numbers;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

}
