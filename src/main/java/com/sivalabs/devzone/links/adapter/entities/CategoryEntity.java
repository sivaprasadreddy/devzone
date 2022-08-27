package com.sivalabs.devzone.links.adapter.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sivalabs.devzone.common.BaseEntity;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Setter
@Getter
public class CategoryEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cat_id_generator")
    @SequenceGenerator(name = "cat_id_generator", sequenceName = "cat_id_seq")
    private Long id;

    @Column(nullable = false, unique = true)
    @NotEmpty()
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private Set<LinkEntity> links;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoryEntity other)) {
            return false;
        }
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Set<LinkEntity> getLinks() {
        if (this.links == null) {
            this.links = new HashSet<>();
        }
        return this.links;
    }
}
