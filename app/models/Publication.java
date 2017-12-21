package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;
import java.util.Set;

import javax.persistence.*;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Publication {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    public String pubType;
    @ManyToMany
    public List<Author> authors;
    @Lob
    public String title;
    @ManyToOne
    public Source source;
    public String language;
    public String docType;
    @ManyToMany
    public List<Keyword> keywords;
    @Lob
    public String docAbstract;
    @ManyToMany
    public Set<Citation> citations;
    public Integer year;
    public Integer volume;
    public Integer startPage;
    public Integer endPage;
    public Integer pages;
}
