package io.github.LuizMartendal.library.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CredentialsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;
}
