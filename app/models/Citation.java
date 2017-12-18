package models;

import javax.persistence.*;

@Entity
public class Citation {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    public String author;
    public Integer year;
    public String name;
    public Integer volume;
    public Integer page;

}
