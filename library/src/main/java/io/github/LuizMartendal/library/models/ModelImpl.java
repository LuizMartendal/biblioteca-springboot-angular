package io.github.LuizMartendal.library.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
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
    private LocalDate createdIn;

    @Column(columnDefinition = "date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate updatedIn;

    @Column
    private String createdBy;

    @Column
    private String updatedBy;

    @PrePersist
    private void prePersist() {
        createdIn = LocalDate.now();
        updatedIn = LocalDate.now();

        createdBy = "Not authenticated";
        updatedBy = "Not authenticated";
    }
}
