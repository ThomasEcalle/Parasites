package com.parasites.annotations;

import com.parasites.engine.pieces.Parasite;

import java.lang.reflect.Field;

/**
 * Created by Thomas Ecalle on 06/04/2017.
 */
public class ParasitesAnnotationsParser
{
    public static void analyse(final Class c)
    {
        if (Parasite.class.isAssignableFrom(c))
        {
            final SoundEffect sound = (SoundEffect) c.getAnnotation(SoundEffect.class);

            if (sound != null)
            {
                handleSoundAnnotation(sound, c);
            }
        }
    }

    private static void handleSoundAnnotation(final SoundEffect sound, final Class c)
    {
        try
        {
           //final Object o = c.newInstance();
            Field field = getField("soundEffect", c.getSuperclass());
            field.setAccessible(true);
            field.set(c.newInstance(), sound.value());

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static Field getField(final String fieldName, final Class c)
    {
        try
        {
            return c.getField(fieldName);
        } catch (NoSuchFieldException e)
        {
            final Class<?> superClass = c.getSuperclass();
            if (superClass == null)
            {
                //System.out.println(e);
            } else
            {
                return getField(fieldName, superClass);
            }
        }
        return null;
    }
}
