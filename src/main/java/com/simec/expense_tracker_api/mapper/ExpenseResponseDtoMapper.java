package com.simec.expense_tracker_api.mapper;

import com.simec.expense_tracker_api.DateConverter;
import com.simec.expense_tracker_api.dto.ExpenseResponseDto;
import com.simec.expense_tracker_api.entity.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExpenseResponseDtoMapper {
    private final DateConverter converter;

    @Autowired
    public ExpenseResponseDtoMapper(DateConverter converter) {
        this.converter = converter;
    }

    public ExpenseResponseDto toDto(Expense expense) {
        return new ExpenseResponseDto(
                expense.id(),
                expense.name(),
                converter.toLocalDate(expense.createdAt()),
                converter.toLocalDate(expense.updatedAt()),
                converter.toLocalDate(expense.appliedAt()),
                expense.category().name()
        );
    }

    public List<ExpenseResponseDto> toDtoList(List<Expense> expense) {
        return expense.stream()
                .map(this::toDto)
                .toList();
    }
}
