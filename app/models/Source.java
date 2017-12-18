package models;

import javax.persistence.*;

@Entity
public class Source {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    public String name;
    public String ISSN;
}
