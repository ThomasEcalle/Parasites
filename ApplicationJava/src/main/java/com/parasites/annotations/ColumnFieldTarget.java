package com.parasites.annotations;

import java.lang.annotation.*;

/**
 * Created by Thomas Ecalle on 18/05/2017.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * This Annotation is use in order to automatically set the FX columns properties
 */

public @interface ColumnFieldTarget
{
    String value();
}
