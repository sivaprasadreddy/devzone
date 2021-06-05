package com.sivalabs.devzone.domain.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "links")
public class Link extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "link_id_generator")
    @SequenceGenerator(
            name = "link_id_generator",
            sequenceName = "link_id_seq",
            allocationSize = 100)
    private Long id;

    @Column(nullable = false)
    @NotEmpty()
    private String url;

    @Column(nullable = false)
    @NotEmpty()
    private String title;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "link_tag",
            joinColumns = {@JoinColumn(name = "link_id", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "ID")})
    private Set<Tag> tags;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Link)) {
            return false;
        }

        Link other = (Link) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void addTag(Tag tag) {
        if (this.tags == null) {
            this.tags = new HashSet<>();
        }
        this.tags.add(tag);
        tag.getLinks().add(this);
    }
}
