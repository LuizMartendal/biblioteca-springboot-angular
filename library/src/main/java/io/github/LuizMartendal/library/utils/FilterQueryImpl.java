package io.github.LuizMartendal.library.utils;

import io.github.LuizMartendal.library.enuns.FilterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterQueryImpl {

    private String field;
    private FilterType type;

    private String stringValue;
    private Long longValue;
    private Date dateValue;
    private Boolean booleanValue;
}
