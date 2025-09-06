package com.simec.expense_tracker_api.controller;

import com.simec.expense_tracker_api.dto.ExpenseResponseDto;
import com.simec.expense_tracker_api.entity.Category;
import com.simec.expense_tracker_api.entity.Expense;
import com.simec.expense_tracker_api.filtering.FilterService;
import com.simec.expense_tracker_api.mapper.ExpenseResponseDtoMapper;
import com.simec.expense_tracker_api.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;

@WebMvcTest(ExpenseController.class)
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenseService expenseService;

    @MockitoBean
    private ExpenseResponseDtoMapper mapper;

    @MockitoBean
    private FilterService filterService;

    @WithMockUser
    @Test
    void expenseExistsWhenDetailIsRequestedThenExpenseIsReturned() throws Exception {
        Expense expense = new Expense(1, "Espresso", LocalDate.now(), new Category(1, "Coffe"));

        Mockito.when(expenseService.find(expense.id())).thenReturn(expense);
        Mockito.when(mapper.toDto(expense)).thenReturn(
                new ExpenseResponseDto(expense.id(), expense.name(), expense.appliedAt(), expense.category().name()));

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/expenses/" + expense.id()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expense.id()))
                .andExpect(jsonPath("$.name").value(expense.name()))
                .andExpect(jsonPath("$.appliedAt").value(expense.appliedAt().toString()))
                .andExpect(jsonPath("$.category").value(expense.category().name()));
    }
}