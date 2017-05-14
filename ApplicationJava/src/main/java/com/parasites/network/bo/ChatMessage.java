package com.parasites.network.bo;

/**
 * Created by Thomas Ecalle on 14/05/2017.
 */
public final class ChatMessage
{
    private User author;
    private String message;


    public ChatMessage(final User author, final String message)
    {
        this.author = author;
        this.message = message;
    }

    @Override
    public String toString()
    {
        return author + " : " + message;
    }

    public User getAuthor()
    {
        return author;
    }

    public String getMessage()
    {
        return message;
    }
}
