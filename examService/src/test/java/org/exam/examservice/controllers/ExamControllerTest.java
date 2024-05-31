package org.exam.examservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.exam.examservice.dto.QuestionDTO;
import org.exam.examservice.dto.RequestAddQuestionsDTO;
import org.exam.examservice.dto.RequestQuestionsDTO;
import org.exam.examservice.dto.ResponseQuestionsDTO;
import org.exam.examservice.responses.success.DataCreatedResponse;
import org.exam.examservice.service.ExamService;
import org.exam.examservice.utils.QuestionLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ExamController.class)
class ExamControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExamService examService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws Exception {
        when(examService.getAvailableDisciplines()).thenReturn(List.of("math", "history"));
    }

    @Test
    public void whenRequestAvailableDisciplinesThenReturnRightResponse() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/api/disciplines"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("math"));
        assertTrue(result.getResponse().getContentAsString().contains("history"));
    }

    @Test
    public void whenRequestQuestionsThenReturnRightResponse() throws Exception {
        List<RequestQuestionsDTO> request = List.of(
                new RequestQuestionsDTO("math", QuestionLevel.easy, 2),
                new RequestQuestionsDTO("history", QuestionLevel.medium, 4)
        );

        List<ResponseQuestionsDTO> response = List.of(
                new ResponseQuestionsDTO(
                        "math",
                        Set.of(
                                QuestionDTO.builder().question("1").answer("1").level(QuestionLevel.easy).build(),
                                QuestionDTO.builder().question("2").answer("2").level(QuestionLevel.easy).build()
                        )
                ),
                new ResponseQuestionsDTO(
                        "history",
                        Set.of(
                                QuestionDTO.builder().question("1").answer("1").level(QuestionLevel.medium).build(),
                                QuestionDTO.builder().question("2").answer("2").level(QuestionLevel.medium).build(),
                                QuestionDTO.builder().question("3").answer("3").level(QuestionLevel.medium).build(),
                                QuestionDTO.builder().question("4").answer("4").level(QuestionLevel.medium).build()
                        )
                )
        );

        when(examService.getRequestedQuestions(request)).thenReturn(
                response
        );

        MvcResult result = this.mockMvc.perform(
                post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
        ).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(response, objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, ResponseQuestionsDTO.class)
        ));
    }

    @Test
    public void whenRequestQuestionsWithInvalidBodyThenBadResponseReturned() throws Exception {
        String jsonRequest = """
                [
                    {
                        "level": "easy",
                        "amount": 2
                    },
                    {
                        "discipline": "history",
                        "level": "medium",
                        "amount": 4
                    }
                ]
                """;
        MvcResult result = this.mockMvc.perform(
                post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
        ).andExpectAll(
                status().isBadRequest()
        ).andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    public void whenRequestToAddQuestionsThenReturnRightResponse() throws Exception {
        List<RequestAddQuestionsDTO> request = List.of(
                new RequestAddQuestionsDTO(
                        "math",
                        List.of(
                                QuestionDTO.builder().question("1").answer("1").level(QuestionLevel.easy).build()
                        )
                ),
                new RequestAddQuestionsDTO(
                        "history",
                        List.of(
                                QuestionDTO.builder().question("2").answer("2").level(QuestionLevel.medium).build()
                        )
                )
        );

        List<DataCreatedResponse> response = List.of(
                new DataCreatedResponse(201, "New questions added successfully"),
                new DataCreatedResponse(201, "New questions added successfully")
        );

        when(examService.addNewQuestions(request)).thenReturn(response);

        MvcResult result = this.mockMvc.perform(
                post("/api/addQuestions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
        ).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(response, objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, DataCreatedResponse.class)
        ));
    }

    @Test
    public void whenRequestToAddQuestionsWithInvalidBodyThenBadRequestReturned() throws Exception {
        String jsonRequest = """
                [
                    {
                        "questions": [
                            {
                                "question": "1",
                                "answer": "1",
                                "level": "easy"
                            }
                        ]
                    },
                    {
                        "discipline": "history",
                        "questions": [
                            {
                                "question": "2",
                                "answer": "2",
                                "level": "medium"
                            }
                        ]
                    }
                ]
                """;

        MvcResult result = this.mockMvc.perform(
                post("/api/addQuestions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
        ).andExpectAll(
                status().isBadRequest()
        ).andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }
}