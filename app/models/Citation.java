package models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Citation {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    public String author;
    public Integer year;
    public String name;
    public Integer volume;
    public Integer page;
    @ManyToMany(mappedBy="citations")
    @JsonIgnore
    public List<Publication> publications;

}
