package io.github.LuizMartendal.library.models.entities;

import io.github.LuizMartendal.library.models.ModelImpl;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "people")
@Table
@Data
public class Person extends ModelImpl implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "First name cannot be null")
    @NotEmpty(message = "First name cannot be empty")
    @Column
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @NotEmpty(message = "Last name cannot be empty")
    @Column
    private String lastName;

    @NotNull(message = "Address name cannot be null")
    @NotEmpty(message = "Address name cannot be empty")
    @Column
    private String address;

    @NotNull(message = "Gender cannot be null")
    @NotEmpty(message = "Gender cannot be empty")
    @Column
    private String gender;
}
