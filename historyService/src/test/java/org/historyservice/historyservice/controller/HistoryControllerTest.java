package org.historyservice.historyservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.historyservice.historyservice.dto.HistoryDTO;
import org.historyservice.historyservice.services.HistoryService;
import org.historyservice.historyservice.utils.QuestionLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HistoryController.class)
class HistoryControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoryService historyService;

    @BeforeEach
    public void setUp() {
        List<HistoryDTO> dtos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dtos.add(
                    HistoryDTO.builder().question("Question " + i).answer("Answer " + i).level(QuestionLevel.easy).build()
            );
        }

        when(historyService.getAllQuestionsByLevel(QuestionLevel.easy))
                .thenReturn(
                        dtos
                );

        doNothing().when(historyService).addNewQuestions(any());

        when(historyService.getRandomQuestions(5))
                .thenReturn(
                        dtos.subList(0, 5)
                );

        when(historyService.getRandomQuestions(3))
                .thenReturn(
                        dtos.subList(0, 3)
                );

        when(historyService.getRandomQuestionsByLevel(QuestionLevel.easy, 6))
                .thenReturn(
                        dtos.subList(0, 6)
                );
    }

    @Test
    public void whenRequestRandomQuestionsThenReturnRightResponse() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/api"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andReturn();

        List<HistoryDTO> dtoResult = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, HistoryDTO.class)
        );
        System.out.println(dtoResult);

        assertEquals(5, dtoResult.size());
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void whenRequestThreeRandomQuestionsThenReturnRightResponse() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/api?amount=3"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andReturn();

        List<HistoryDTO> dtoResult = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, HistoryDTO.class)
        );
        System.out.println(dtoResult);

        assertEquals(3, dtoResult.size());
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void whenRequestAllQuestionsByLevelThenReturnRightResponse() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/api/easy"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andReturn();

        List<HistoryDTO> dtoResult = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, HistoryDTO.class)
        );

        assertEquals(10, dtoResult.size());
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void whenRequestAllQuestionsByIncorrectLevelThenReturnedBadRequestStatus() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/api/wrongLevel"))
                .andExpectAll(
                        status().isBadRequest()
                ).andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    public void whenRequestRandomQuestionsByLevelThenReturnRightResponse() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/api/easy?amount=6"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andReturn();

        List<HistoryDTO> dtoResult = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, HistoryDTO.class)
        );

        assertEquals(6, dtoResult.size());
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void whenRequestToAddNewQuestionsThenReturnRightResponse() throws Exception {
        List<HistoryDTO> newQuestions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            newQuestions.add(
                    HistoryDTO.builder().question("Question " + i).answer("Answer " + i).level(QuestionLevel.medium).build()
            );
        }

        MvcResult result = this.mockMvc.perform(
                post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(newQuestions))
        ).andExpectAll(
                status().isCreated(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("New questions added successfully"));
    }
}