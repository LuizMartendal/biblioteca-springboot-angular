package io.github.LuizMartendal.library.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandError implements Serializable {

    private static final long serialVersionUID = 1L;

    private String status;
    private String message;
    private int code;
    private long timestamp;
}
