package io.github.LuizMartendal.library.models;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@MappedSuperclass
public class ModelImpl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "char(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    protected UUID id;

    @Column
    private Date createdIn;

    @Column
    private Date updatedIn;

    @Column
    private String createdBy;

    @Column
    private String updatedBy;

    @PrePersist
    private void prePersist() {
        createdIn = new Date();
        updatedIn = new Date();

        createdBy = "Not authenticated";
        updatedBy = "Not authenticated";
    }
}
