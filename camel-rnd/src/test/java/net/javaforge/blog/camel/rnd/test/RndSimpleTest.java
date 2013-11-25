package net.javaforge.blog.camel.rnd.test;

import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class RndSimpleTest extends CamelTestSupport {

    private static final int expectedMessageCount = 3;

    private static final int expectedMessageLength = 5;

    @Test
    public void testRandomChars() throws Exception {
        testWithUriAndRegex(
                "rnd:foo?initialDelay=0&generator=random&chars=abc&length="
                        + expectedMessageLength, "[abc]{"
                + expectedMessageLength + "}");
    }

    @Test
    public void testNumeric() throws Exception {
        testWithUriAndRegex("rnd:foo?initialDelay=0&generator=numeric&length="
                + expectedMessageLength, "\\p{Digit}{" + expectedMessageLength
                + "}");
    }

    @Test
    public void testAlphanumeric() throws Exception {
        testWithUriAndRegex(
                "rnd:foo?initialDelay=0&generator=alphanumeric&length="
                        + expectedMessageLength, "\\p{Alnum}{"
                + expectedMessageLength + "}");
    }

    @Test
    public void testAlphabetic() throws Exception {
        testWithUriAndRegex(
                "rnd:foo?initialDelay=0&generator=alphabetic&length="
                        + expectedMessageLength, "\\p{Alpha}{"
                + expectedMessageLength + "}");
    }

    @Test
    public void testAscii() throws Exception {
        testWithUriAndRegex("rnd:foo?initialDelay=0&generator=ascii&length="
                + expectedMessageLength, "\\p{ASCII}{" + expectedMessageLength
                + "}");
    }

    private void testWithUriAndRegex(final String uri, final String regex)
            throws Exception {

        context.getRouteDefinitions().get(0).adviceWith(context,
                new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        replaceFromWith(uri);
                    }
                });

        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.expectedMinimumMessageCount(expectedMessageCount);

        // we expect all message bodies to be an instance of String
        mockOut.allMessages().body().isInstanceOf(String.class);

        // we expect all message bodies start with "fired..." text
        mockOut.allMessages().body().regex(regex);

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("rnd:foo").to("mock:out");
            }
        };
    }

}
