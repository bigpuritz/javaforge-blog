package net.javaforge.blog.spring.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * @author Maxim Kalina
 * @version $Id$
 */
@Component
public class ProfilingMethodInterceptor implements MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ProfilingMethodInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        final StopWatch stopWatch = new StopWatch(invocation.getMethod().toGenericString());
        stopWatch.start("invocation.proceed()");

        try {
            log.info(">>>>>>> START METHOD {} <<<<<<<<<", invocation.getMethod().toGenericString());
            return invocation.proceed();
        } finally {
            stopWatch.stop();
            log.info(stopWatch.prettyPrint());
            log.info(">>>>>>> END METHOD {} <<<<<<<<<", invocation.getMethod().toGenericString());
        }

    }

}
