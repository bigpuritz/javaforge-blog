package net.javaforge.blog.spring.aop;

import java.lang.annotation.*;

/**
 * @author Maxim Kalina
 * @version $Id$
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface ProfileExecution {


}
