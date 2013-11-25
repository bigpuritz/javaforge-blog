package net.javaforge.blog.camel.rnd.test;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Scanner;

/**
 *
 */
public class RndFileTest extends CamelTestSupport {

    private static final int expectedMessageCount = 20;

    private static final int expectedMessageLength = 50;

    private static final String expectedFilename = "target/out.txt";

    @Before
    public void setupTest() {
        File f = new File(expectedFilename);
        if (f.exists())
            f.delete();
    }

    @Test
    public void testWriteToFile() throws Exception {

        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.expectedMinimumMessageCount(expectedMessageCount);

        assertMockEndpointsSatisfied();

        assertFileExists(expectedFilename);

        Scanner scanner = new Scanner(new File(expectedFilename));
        int counter = 0;
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            assertNotNull(line);
            assertEquals(expectedMessageLength, line.length());
            counter++;
        }
        assertEquals(expectedMessageCount, counter);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from(
                        "rnd:foo?generator=alphanumeric&delay=10&length="
                                + expectedMessageLength).process(
                        new Processor() {

                            @Override
                            public void process(Exchange exchange)
                                    throws Exception {
                                exchange.getIn().setBody(
                                        exchange.getIn().getBody() + "\n");
                            }
                        }).to("file:target?fileName=out.txt&fileExist=Append")
                        .to("mock:out");
            }

        };
    }
}
