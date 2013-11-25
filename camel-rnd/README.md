This project demonstrates how to create a simple Apache Camel component.
It generates random character sequences that can be consumed by the subsequent route nodes.

Component's URI format:

```
rnd:someName[?options]
```

Options:

| Name | Default Value | Description
--- | --- | ---
generator | random | possible values: random, alphabetic, alphanumeric, numeric, ascii
length | 10 | length of the generated character sequence
chars | | characters to use for generation. available only for generator == random
letters | false | use letters for generations. available only for generator == random
numbers | false | use numbers for generations. available only for generator == random
start | 0 | the position in set of chars to start at. available only for generator == random
end | 0 | the position in set of chars to end before. available only for generator == random


Usage examples:

Repeatedly generate random alphabetic character sequences each 5 characters long
and send them to the console output stream.

```java
from("rnd:foo?generator=alphabetic&length=30").to("stream:out");
```

Repeatedly generate random alphanumeric character sequences each 100 characters long,
aggregate them to the lists of 5 elements and send each list as a new message to the subsequent log component.

```java
from("rnd:foo?generator=alphanumeric&initialDelay=0&delay=50&length=100")
        .setHeader("foo", constant("bar"))
        .aggregate(header("foo"), new ListAggregationStrategy())
        .completionSize(5)
        .to("log:net.javaforge.blog.camel?level=INFO");


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
```

Repeatedly generate random alphanumeric character sequences each 50 characters long and
append them to the existing file out.txt

```java
from("rnd:foo?generator=alphanumeric&delay=10&length=50)
    .process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody(exchange.getIn().getBody() + "\n");
            }
        })
    .to("file:target?fileName=out.txt&fileExist=Append")
```
