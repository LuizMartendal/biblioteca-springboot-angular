package io.github.LuizMartendal.library.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.LuizMartendal.library.models.ModelImpl;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
@Schema(name = "Book", description = "Book entity")
public class Book extends ModelImpl implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Author field cannot be null")
    @NotEmpty(message = "Author field cannot be empty")
    @Column
    private String author;

    @NotNull(message = "Launch date field cannot be null")
    @Column(columnDefinition = "date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date launchDate;

    @NotNull(message = "Price field cannot be null")
    @Column(columnDefinition = "decimal(65,2)")
    private Double price;

    @NotNull(message = "Title field cannot be null")
    @NotEmpty(message = "Title field cannot be empty")
    @Column
    private String title;

}
