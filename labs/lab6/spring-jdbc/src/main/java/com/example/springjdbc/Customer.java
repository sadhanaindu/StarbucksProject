package com.example.springjdbc;

public class Customer {
    private long id;
    private String firstName, lastName;

    public Customer(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }

    public long getId()
    {
        return id;
    }
    public void setId(long ID)
    {
        id = ID;
    }
    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String first)
    {
        firstName = first;
    }

    public String getLastName()
    {
        return lastName;
    }
    public void setLastName(String last)
    {
        lastName = last;
    }

}