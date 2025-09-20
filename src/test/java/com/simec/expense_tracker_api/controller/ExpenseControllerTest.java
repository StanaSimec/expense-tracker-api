package com.simec.expense_tracker_api.controller;

import com.simec.expense_tracker_api.dao.CategoryDao;
import com.simec.expense_tracker_api.dto.ExpenseRequestDto;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExpenseController.class)
class ExpenseControllerTest {

    private static final Expense EXPENSE = new Expense(1, "Espresso",
            LocalDate.of(2024, 2, 4),
            new Category(1, "Coffe"));
    private static final String URL = "/v1/expenses";
    private static final String REQUEST_CONTENT = """
            {
                "name": "%s",
                "appliedAt": "%s",
                "category": "%s"
            }
            """.formatted(
            EXPENSE.name(),
            EXPENSE.appliedAt().format(DateTimeFormatter.ISO_LOCAL_DATE),
            EXPENSE.category().name());
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ExpenseService expenseService;
    @MockitoBean
    private ExpenseResponseDtoMapper mapper;
    @MockitoBean
    private FilterService filterService;
    @MockitoBean
    private CategoryDao categoryDao;

    @WithMockUser
    @Test
    void whenDetailIsRequestedThenExpenseIsReturned() throws Exception {

        Mockito.when(expenseService.find(EXPENSE.id())).thenReturn(EXPENSE);
        Mockito.when(mapper.toDto(EXPENSE)).thenReturn(
                new ExpenseResponseDto(EXPENSE.id(), EXPENSE.name(), EXPENSE.appliedAt(), EXPENSE.category().name()));

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/" + EXPENSE.id()))
                .andExpect(status().isOk());
        assertResponseBody(actions);
    }

    @WithMockUser
    @Test
    void whenPostIsSendThenCreatedExpenseIsReturned() throws Exception {

        Mockito.when(categoryDao.findByName(EXPENSE.category().name())).thenReturn(Optional.of(EXPENSE.category()));
        Mockito.when(expenseService.create(
                        new ExpenseRequestDto(EXPENSE.name(), EXPENSE.appliedAt(), EXPENSE.category().name())))
                .thenReturn(EXPENSE);
        Mockito.when(mapper.toDto(EXPENSE)).thenReturn(
                new ExpenseResponseDto(EXPENSE.id(), EXPENSE.name(), EXPENSE.appliedAt(), EXPENSE.category().name()));

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(REQUEST_CONTENT)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertResponseBody(actions);
    }

    @WithMockUser
    @Test
    void whenUpdateIsSendThenUpdatedExpenseIsReturned() throws Exception {
        Mockito.when(categoryDao.findByName(EXPENSE.category().name())).thenReturn(Optional.of(EXPENSE.category()));
        Mockito.when(expenseService.update(
                        new ExpenseRequestDto(EXPENSE.name(), EXPENSE.appliedAt(), EXPENSE.category().name()),
                        EXPENSE.id()))
                .thenReturn(EXPENSE);
        Mockito.when(mapper.toDto(EXPENSE)).thenReturn(
                new ExpenseResponseDto(EXPENSE.id(), EXPENSE.name(), EXPENSE.appliedAt(), EXPENSE.category().name()));

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.put(URL + "/" + EXPENSE.id())
                        .content(REQUEST_CONTENT)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertResponseBody(actions);
    }

    @WithMockUser
    @Test
    void whenDeleteIsSendThenDeletedStatusIsReturned() throws Exception {
        Mockito.when(expenseService.find(EXPENSE.id())).thenReturn(EXPENSE);
        Mockito.when(mapper.toDto(EXPENSE)).thenReturn(
                new ExpenseResponseDto(EXPENSE.id(), EXPENSE.name(), EXPENSE.appliedAt(), EXPENSE.category().name()));

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/" + EXPENSE.id()).with(csrf()))
                .andExpect(status().isNoContent());

        Mockito.verify(expenseService, Mockito.times(1)).delete(EXPENSE.id());
    }

    private void assertResponseBody(ResultActions resultActions) throws Exception {
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(EXPENSE.id()))
                .andExpect(jsonPath("$.name").value(EXPENSE.name()))
                .andExpect(jsonPath("$.appliedAt").value(EXPENSE.appliedAt().toString()))
                .andExpect(jsonPath("$.category").value(EXPENSE.category().name()));
    }
}