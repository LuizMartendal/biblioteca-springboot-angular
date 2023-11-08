package io.github.LuizMartendal.library.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@XmlRootElement
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String user;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date created;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date expiredIn;

    private String token;
}
