package models;

import javax.persistence.*;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    public String shortName;
    public String fullName;
    public String affiliation;
}
