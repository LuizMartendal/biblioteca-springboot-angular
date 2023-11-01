package io.github.LuizMartendal.library.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> {

    private List<T> content;
    private Long totalElements;
    private Long totalPages;

}