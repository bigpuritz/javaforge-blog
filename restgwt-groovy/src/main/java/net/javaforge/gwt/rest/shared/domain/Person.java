package net.javaforge.gwt.rest.shared.domain;

import java.util.Date;

public class Person {

    private int id;

    private String name;

    private Date dateOfBirth;

    public Person() {
    }

    public Person(int id, String name, Date dateOfBirth) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "Person [id=" + this.id + ", name=" + this.name
                + ", dateOfBirth=" + this.dateOfBirth + "]";
    }

}
