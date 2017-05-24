package com.parasites.network.bo;

/**
 * Created by Thomas Ecalle on 13/05/2017.
 */
public final class User
{
    private int id;
    private String pseudo;
    private String firstname;
    private String lastname;
    private String password;
    private String phone_number;
    private String token;
    private String email;

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

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getPhone_number()
    {
        return phone_number;
    }

    public void setPhone_number(String phone_number)
    {
        this.phone_number = phone_number;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getLastname()
    {
        return lastname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public int getId()
    {
        return id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
