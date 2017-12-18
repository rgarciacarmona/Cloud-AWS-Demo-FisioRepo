package models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Keyword {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    public String name;
    @ManyToMany(mappedBy="keywords", fetch=FetchType.EAGER)
    public List<Publication> publications;
}
