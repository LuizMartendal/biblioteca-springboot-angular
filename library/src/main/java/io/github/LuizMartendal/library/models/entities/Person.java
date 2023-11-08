package io.github.LuizMartendal.library.models.entities;

import io.github.LuizMartendal.library.enuns.Roles;
import io.github.LuizMartendal.library.models.ModelImpl;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@EqualsAndHashCode(callSuper = true)
@Entity(name = "person")
@NoArgsConstructor
@AllArgsConstructor
@Table
@Data
@Schema(name = "Person", description = "Person entity")
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

    @NotNull(message = "User name cannot be null")
    @NotEmpty(message = "User name cannot be empty")
    @Column
    private String username;

    @NotNull(message = "Password cannot be null")
    @NotEmpty(message = "Passsword name cannot be empty")
    @Length(min = 4, message = "Password must contain 4 characters at least")
    @Column
    private String password;

    @NotNull(message = "Address name cannot be null")
    @NotEmpty(message = "Address name cannot be empty")
    @Column
    private String address;

    @NotNull(message = "Gender cannot be null")
    @NotEmpty(message = "Gender cannot be empty")
    @Column
    private String gender;

    @Column
    @Enumerated(EnumType.STRING)
    private Roles role;
}
