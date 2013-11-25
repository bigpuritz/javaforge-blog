package net.javaforge.blog.camel.rnd.test;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RndWithAggregatorTest extends CamelTestSupport {

    private static final int expectedMessageCount = 20;

    private static final int expectedListSize = 5;

    private static final int expectedMessageLength = 5;

    @Test
    public void testAggregatedList() throws Exception {
        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.expectedMinimumMessageCount(expectedMessageCount);

        mockOut.whenAnyExchangeReceived(new Processor() {

            @SuppressWarnings("unchecked")
            @Override
            public void process(Exchange exchange) throws Exception {
                assertNotNull(exchange.getIn().getBody());
                assertIsInstanceOf(List.class, exchange.getIn().getBody());
                assertListSize(exchange.getIn().getBody(List.class),
                        expectedListSize);

                List<String> data = exchange.getIn().getBody(List.class);
                for (String item : data) {
                    assertNotNull(item);
                    assertEquals(expectedMessageLength, item.length());
                }

            }
        });

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("rnd:foo?generator=alphanumeric&initialDelay=0&delay=50&length=" + expectedMessageLength)
                        .setHeader("foo", constant("bar"))
                        .aggregate(header("foo"), new ListAggregationStrategy())
                        .completionSize(expectedListSize)
                        .to("log:net.javaforge.blog.camel?level=INFO")
                        .to("mock:out");
            }
        };
    }

    private static class ListAggregationStrategy implements AggregationStrategy {

        @SuppressWarnings("unchecked")
        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            Object newBody = newExchange.getIn().getBody();
            List<Object> list;
            if (oldExchange == null) {
                list = new ArrayList<Object>();
                list.add(newBody);
                newExchange.getIn().setBody(list);
                return newExchange;
            } else {
                list = oldExchange.getIn().getBody(List.class);
                list.add(newBody);
                return oldExchange;
            }
        }
    }
}
