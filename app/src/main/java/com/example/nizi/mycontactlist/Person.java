package com.example.nizi.mycontactlist;

import java.io.Serializable;
import java.util.ArrayList;

public class Person implements Serializable {
    public String name;
    public String Phone;
    public ArrayList<Person> relationship;

    public Person(){}

    public Person(String name, String Phone, ArrayList<Person> relationship){
        this.name = name;
        this.Phone = Phone;
        this.relationship = relationship;
    }
    @Override
    public String toString() {
        return this.name + "   " + this.Phone;
    }
}
