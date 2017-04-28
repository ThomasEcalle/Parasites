package com.parasites.annotations;

import java.lang.annotation.*;

/**
 * Created by Thomas Ecalle on 05/04/2017.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SoundEffect
{
    String value();
}
