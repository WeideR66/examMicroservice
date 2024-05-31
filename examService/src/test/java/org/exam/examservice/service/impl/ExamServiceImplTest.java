package org.exam.examservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.EurekaClient;
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
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RestClientTest(ExamService.class)
@AutoConfigureWebClient(registerRestTemplate = true)
class ExamServiceImplTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private EurekaClient eurekaClient;

    @SpyBean
    private ExamServiceImpl examService;

    @Autowired
    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        doReturn(List.of("math", "history")).when(examService).getAvailableDisciplines();
    }

    @Test
    public void whenGetAvailableDisciplinesThenReturnedRightValue() {
        List<String> disciplines = examService.getAvailableDisciplines();

        assertEquals(2, disciplines.size());
        assertTrue(disciplines.contains("math"));
        assertTrue(disciplines.contains("history"));
    }

    @Test
    public void whenGetRequestedQuestionsThenReturnRightDataAndSendsRightRequests() {
        List<RequestQuestionsDTO> requestQuestionsDTO = List.of(
                new RequestQuestionsDTO("math", QuestionLevel.easy, 2),
                new RequestQuestionsDTO("history", QuestionLevel.hard, 2)
        );
        String jsonResponseMath = """
                [
                    {
                        "question": "1",
                        "answer": "1",
                        "level": "easy"
                    },
                    {
                        "question": "2",
                        "answer": "2",
                        "level": "easy"
                    }
                ]
                """;
        String jsonResponseHistory = """
                [
                    {
                        "question": "3",
                        "answer": "3",
                        "level": "hard"
                    },
                    {
                        "question": "4",
                        "answer": "4",
                        "level": "hard"
                    }
                ]
                """;
        this.mockServer.expect(requestTo("http://MATH/api/easy?amount=2"))
                .andRespond(withSuccess(jsonResponseMath, MediaType.APPLICATION_JSON));
        this.mockServer.expect(requestTo("http://HISTORY/api/hard?amount=2"))
                .andRespond(withSuccess(jsonResponseHistory, MediaType.APPLICATION_JSON));

        List<ResponseQuestionsDTO> result = examService.getRequestedQuestions(requestQuestionsDTO);

        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals("math", result.get(0).getDiscipline()),
                () -> assertEquals("history", result.get(1).getDiscipline()),
                () -> assertEquals(2, result.get(0).getQuestions().size()),
                () -> assertEquals(2, result.get(1).getQuestions().size()),
                () -> assertEquals(2, result.get(0).getQuestions().stream().filter(question -> question.getLevel() == QuestionLevel.easy).count()),
                () -> assertEquals(2, result.get(1).getQuestions().stream().filter(question -> question.getLevel() == QuestionLevel.hard).count())
        );

    }

    @Test
    public void whenAddQuestionsThenServiceSendRightRequests() throws Exception {
        List<RequestAddQuestionsDTO> request = List.of(
                new RequestAddQuestionsDTO(
                        "math",
                        List.of(
                                QuestionDTO.builder().question("1").answer("2").level(QuestionLevel.easy).build(),
                                QuestionDTO.builder().question("2").answer("2").level(QuestionLevel.medium).build()
                        )
                ),
                new RequestAddQuestionsDTO(
                        "history",
                        List.of(
                                QuestionDTO.builder().question("3").answer("3").level(QuestionLevel.hard).build(),
                                QuestionDTO.builder().question("4").answer("4").level(QuestionLevel.hard).build(),
                                QuestionDTO.builder().question("5").answer("5").level(QuestionLevel.medium).build()
                        )
                )
        );

        String responseMathJson = """
                {
                    "status": 201,
                    "message": "Вопросы для math сервиса добавлены"
                }
                """;
        String responseHistoryJson = """
                {
                    "status": 201,
                    "message": "Вопросы для history сервиса добавлены"
                }
                """;

        this.mockServer.expect(requestTo("http://MATH/api"))
                .andExpect(method(POST))
                .andExpect(content().string(mapper.writeValueAsString(request.get(0).getQuestions())))
                .andRespond(withStatus(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(responseMathJson));

        this.mockServer.expect(requestTo("http://HISTORY/api"))
                .andExpect(method(POST))
                .andExpect(content().string(mapper.writeValueAsString(request.get(1).getQuestions())))
                .andRespond(withStatus(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(responseHistoryJson));

        List<DataCreatedResponse> response = examService.addNewQuestions(request);

        assertAll(
                () -> assertEquals(2, response.stream().filter(resp -> resp.getStatus() == 201).toList().size()),
                () -> assertEquals(
                        "Вопросы для math сервиса добавлены",
                        response.get(0).getMessage()
                ),
                () -> assertEquals(
                        "Вопросы для history сервиса добавлены",
                        response.get(1).getMessage()
                )
        );
    }
}