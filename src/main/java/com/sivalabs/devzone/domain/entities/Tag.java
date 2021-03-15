package com.sivalabs.devzone.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tags")
@Setter
@Getter
public class Tag extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_id_generator")
    @SequenceGenerator(name = "tag_id_generator", sequenceName = "tag_id_seq", allocationSize = 100)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotEmpty()
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private Set<Link> links;

    public Set<Link> getLinks() {
        if (this.links == null) {
            this.links = new HashSet<>();
        }
        return this.links;
    }
}
