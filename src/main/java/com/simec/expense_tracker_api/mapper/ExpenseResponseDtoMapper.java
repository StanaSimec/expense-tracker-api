package com.simec.expense_tracker_api.mapper;

import com.simec.expense_tracker_api.dto.ExpenseResponseDto;
import com.simec.expense_tracker_api.entity.Expense;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExpenseResponseDtoMapper {

    public ExpenseResponseDto toDto(Expense expense) {
        return new ExpenseResponseDto(
                expense.id(),
                expense.name(),
                expense.appliedAt(),
                expense.category().name()
        );
    }

    public List<ExpenseResponseDto> toDtoList(List<Expense> expense) {
        return expense.stream()
                .map(this::toDto)
                .toList();
    }
}
