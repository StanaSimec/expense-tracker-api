package com.simec.expense_tracker_api.controller;

import com.simec.expense_tracker_api.date.DateWrapper;
import com.simec.expense_tracker_api.dto.ExpenseRequestDto;
import com.simec.expense_tracker_api.dto.ExpenseResponseDto;
import com.simec.expense_tracker_api.entity.Expense;
import com.simec.expense_tracker_api.filtering.Filter;
import com.simec.expense_tracker_api.filtering.FilterService;
import com.simec.expense_tracker_api.mapper.ExpenseResponseDtoMapper;
import com.simec.expense_tracker_api.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final ExpenseResponseDtoMapper mapper;
    private final FilterService filterService;
    private final DateWrapper dateWrapper;

    @Autowired
    public ExpenseController(ExpenseService expenseService, ExpenseResponseDtoMapper mapper, FilterService filterFactory,
                             DateWrapper dateProvider) {
        this.expenseService = expenseService;
        this.mapper = mapper;
        this.filterService = filterFactory;
        this.dateWrapper = dateProvider;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDto> create(@Valid @RequestBody ExpenseRequestDto dto) {
        Expense expense = expenseService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(expense));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> find(@PathVariable Long id) {
        Expense expense = expenseService.find(id);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toDto(expense));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> update(@PathVariable Long id, @Valid @RequestBody ExpenseRequestDto dto) {
        Expense expense = expenseService.update(dto, id);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toDto(expense));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> delete(@PathVariable Long id) {
        expenseService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDto>> filter(@RequestParam(required = false) String tag,
                                                           @RequestParam(required = false) String start,
                                                           @RequestParam(required = false) String end) {
        if (tag != null) {
            Filter filter = filterService.getForTag(tag, dateWrapper.now());
            List<Expense> expenses = expenseService.findByFilter(filter);
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toDtoList(expenses));
        }

        if (start != null && end != null) {
            Filter filter = new Filter(dateWrapper.parse(start), dateWrapper.parse(end));
            List<Expense> expenses = expenseService.findByFilter(filter);
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toDtoList(expenses));
        }

        List<Expense> expenses = expenseService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toDtoList(expenses));
    }
}
