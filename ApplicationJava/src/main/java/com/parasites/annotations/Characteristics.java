package com.parasites.annotations;

import java.lang.annotation.*;

/**
 * Created by Thomas Ecalle on 05/04/2017.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Characteristics
{
    int cost() default 10000;

    int creationPoints();

    int attack();

    int defence() default 10000;

    int developmentPointsCost() default 1;
}
