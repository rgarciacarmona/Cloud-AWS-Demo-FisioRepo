package models;

import javax.persistence.*;
import java.util.List;

@Entity
public class FullPublication {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;
    public String pubType;
    @Column( length = 100000 )
    public String authorsShort;
    @Column( length = 100000 )
    public String authorsLong;
    @Column( length = 100000 )
    public String title;
    @Column( length = 100000 )
    public String sourceName;
    public String language;
    public String docType;
    @Column( length = 100000 )
    public String keywords;
    @Column( length = 100000 )
    public String docAbstract;
    @Column( length = 100000 )
    public String authorsAffiliation;
    @Column( length = 100000 )
    public String references;
    public String ISSN;
    public Integer year;
    public Integer volume;
    public Integer startPage;
    public Integer endPage;
    public Integer pages;
}
