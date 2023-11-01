package io.github.LuizMartendal.library.utils;

import io.github.LuizMartendal.library.enuns.FilterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterImpl {

    private List<FilterQueryImpl> filter;
    private Integer size;
    private Integer page;
    private String order;

    public static FilterImpl parse(String filter, Integer size, Integer page, String order) {
        List<FilterQueryImpl> queries = new ArrayList<>();
        if (filter != null && !filter.isEmpty()) {
            String[] filters = filter.split(" AND ");
            for (String fs: filters) {
                String[] fss = fs.split(" ");
                String right = "";
                for (int i = 2; i < fss.length; i++) {
                    if (i != fss.length - 1) {
                        right += fss[i] + " ";
                    } else {
                        right += fss[i];
                    }
                }
                FilterQueryImpl query = new FilterQueryImpl();
                query.setField(fss[0]);
                query.setType(FilterType.valueOf(fss[1]));

                boolean settedValue = false;

                try {
                    query.setLongValue(Long.parseLong(right));
                    settedValue = true;
                } catch (NumberFormatException ignored) {
                }

                if (("true".equals(right) || "false".equals(right)) && !settedValue) {
                    query.setBooleanValue(Boolean.parseBoolean(right));
                    settedValue = true;
                }

                if (!settedValue) {
                    try {
                        if (right.endsWith("Z")) {
                            right = right.replace("Z", "+00:00");
                        }
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                        query.setDateValue(dateFormat.parse(right));
                        settedValue = true;
                    } catch (ParseException e) {
                        right = right.replace("+00:00", "Z");
                    }
                }

                if (!settedValue) {
                    query.setStringValue(right);
                }

                queries.add(query);
            }
        }
        return new FilterImpl(queries, size, page, order);
    }
}