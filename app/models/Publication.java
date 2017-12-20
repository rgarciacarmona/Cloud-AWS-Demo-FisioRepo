package models;

import java.util.List;

import javax.persistence.*;

@Entity
public class Publication {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    public String pubType;
    @ManyToMany
    public List<Author> authors;
    public String title;
    @ManyToOne
    //@JoinColumn(name="source_id")
    public Source source;
    public String language;
    public String docType;
    @ManyToMany
    public List<Keyword> keywords;
    public String docAbstract;
    @ManyToMany
    public List<Citation> citations;
    public Integer year;
    public Integer volume;
    public Integer startPage;
    public Integer endPage;
    public Integer pages;
}
