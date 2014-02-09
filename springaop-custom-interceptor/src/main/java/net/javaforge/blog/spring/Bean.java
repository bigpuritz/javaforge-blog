package net.javaforge.blog.spring;


import net.javaforge.blog.spring.aop.ProfileExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Bean {

    private static final Logger log = LoggerFactory.getLogger(Bean.class);


    @ProfileExecution
    public void foo() {
        log.info("Executing method 'foo'.");
    }


}
