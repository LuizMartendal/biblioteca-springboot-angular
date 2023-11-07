package io.github.LuizMartendal.library.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.LuizMartendal.library.services.entities.PersonService;
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

    @Column(columnDefinition = "date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createdIn;

    @Column(columnDefinition = "date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date updatedIn;

    @Column
    private String createdBy;

    @Column
    private String updatedBy;

    @PrePersist
    private void prePersist() {
        createdIn = new Date();
        updatedIn = new Date();

        try {
            createdBy = PersonService.getLoggedUser();
            updatedBy = PersonService.getLoggedUser();
        } catch (Exception e) {
            createdBy = "Not authenticated";
            updatedBy = "Not authenticated";
        }
    }

    @PostPersist
    private void postPersist() {
        try {
            updatedIn = new Date();
            updatedBy = PersonService.getLoggedUser();
        } catch (Exception e) {
            updatedIn = new Date();
            updatedBy = "Not authenticated";
        }
    }
}
