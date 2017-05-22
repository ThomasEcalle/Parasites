package com.parasites.annotations;

import java.lang.annotation.*;

/**
 * Created by Thomas Ecalle on 17/05/2017.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PressEnter
{
    String value();
}
