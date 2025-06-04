package com.simec.expense_tracker_api.filtering;

import com.simec.expense_tracker_api.exception.FilterNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class FilterService {

    public Filter getForTag(String tag, LocalDate now) {
        FilterType type = findTag(tag);
        return switch (type) {
            case LAST_WEEK -> new Filter(now.minusWeeks(1), now);
            case LAST_MONTH -> new Filter(now.minusMonths(1), now);
            case LAST_THREE_MONTHS -> new Filter(now.minusMonths(3), now);
        };
    }

    public List<String> getAllTags() {
        return Arrays.stream(FilterType.values())
                .map(FilterType::getTag)
                .toList();
    }

    private FilterType findTag(String tag) {
        return Arrays.stream(FilterType.values())
                .filter(f -> f.getTag().equals(tag))
                .findFirst()
                .orElseThrow(() -> new FilterNotFoundException(String.format("%s filter not found.", tag)));
    }
}
