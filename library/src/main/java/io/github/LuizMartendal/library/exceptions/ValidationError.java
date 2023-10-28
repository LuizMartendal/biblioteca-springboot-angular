package io.github.LuizMartendal.library.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError extends StandError implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<FieldMessage> errors = new ArrayList<>();

    public ValidationError(String status, String message, int code, long timestamp) {
        super(status, message, code, timestamp);
    }

    void addError(String fieldString, String message) {
        errors.add(new FieldMessage(fieldString, message));
    }
}
