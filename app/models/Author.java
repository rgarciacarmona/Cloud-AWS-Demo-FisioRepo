package models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    public String shortName;
    public String fullName;
    public String affiliation;
    @ManyToMany(mappedBy="citations")
    public List<Publication> authors;
}
