package com.sivalabs.devzone.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "links")
@Setter
@Getter
public class Link extends BaseEntity implements Serializable {
    @Id
    @SequenceGenerator(name = "link_id_generator", sequenceName = "link_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "link_id_generator")
    private Long id;

    @Column(nullable = false)
    @NotEmpty()
    private String url;

    @Column(nullable = false)
    @NotEmpty()
    private String title;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "link_tag",
        joinColumns = {@JoinColumn(name = "link_id", referencedColumnName = "ID")},
        inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "ID")}
    )
    private Set<Tag> tags;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

}
