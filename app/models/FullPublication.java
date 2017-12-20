package models;

import javax.persistence.*;

@Entity
public class FullPublication {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;
    public String pubType;
    public String authorsShort;
    public String authorsLong;
    public String title;
    public String sourceName;
    public String languages;
    public String docType;
    public String keywords;
    public String docAbstract;
    public String authorsAffiliation;
    public String refs;
    public String ISSN;
    public Integer year;
    public Integer volume;
    public Integer startPage;
    public Integer endPage;
    public Integer pages;
}
