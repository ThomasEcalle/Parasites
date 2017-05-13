package com.parasites.network.bo;

/**
 * Created by Thomas Ecalle on 13/05/2017.
 */
public final class User
{
    private String pseudo;

    public User(final String pseudo)
    {
        this.pseudo = pseudo;
    }

    @Override
    public String toString()
    {
        return pseudo;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof User)) return false;
        final User other = (User) obj;

        return pseudo.equals(other.pseudo);
    }


    public String getPseudo()
    {
        return pseudo;
    }
}
