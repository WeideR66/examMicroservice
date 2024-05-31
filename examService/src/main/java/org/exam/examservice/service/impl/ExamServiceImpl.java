package org.exam.examservice.service.impl;

import com.netflix.discovery.EurekaClient;
import lombok.NoArgsConstructor;
import org.exam.examservice.dto.QuestionDTO;
import org.exam.examservice.dto.RequestAddQuestionsDTO;
import org.exam.examservice.dto.RequestQuestionsDTO;
import org.exam.examservice.dto.ResponseQuestionsDTO;
import org.exam.examservice.responses.success.DataCreatedResponse;
import org.exam.examservice.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@NoArgsConstructor
public class ExamServiceImpl implements ExamService {

    private RestTemplate restTemplate;
    private EurekaClient eurekaClient;

    @Autowired
    public ExamServiceImpl(RestTemplate restTemplate, EurekaClient eurekaClient) {
        this.restTemplate = restTemplate;
        this.eurekaClient = eurekaClient;
    }

    @Override
    public List<ResponseQuestionsDTO> getRequestedQuestions(List<RequestQuestionsDTO> requestQuestionsDTO) {
        List<String> availableDisciplines = getAvailableDisciplines();
        Map<String, Set<QuestionDTO>> questions = new HashMap<>();

        for (RequestQuestionsDTO request : requestQuestionsDTO) {
            if (availableDisciplines.contains(request.getDiscipline())) {
                QuestionDTO[] resp = restTemplate.getForObject(request.getUrl(), QuestionDTO[].class);
                if (resp != null) {
                    if (questions.get(request.getDiscipline()) == null) {
                        Set<QuestionDTO> buffer = new HashSet<>(Arrays.asList(resp));
                        questions.put(request.getDiscipline(), buffer);
                    } else {
                        questions.get(request.getDiscipline()).addAll(Arrays.asList(resp));
                    }
                }
            }
        }

        List<ResponseQuestionsDTO> result = new ArrayList<>();
        for (Map.Entry<String, Set<QuestionDTO>> entry : questions.entrySet()) {
            result.add(
                    ResponseQuestionsDTO
                            .builder()
                            .discipline(entry.getKey())
                            .questions(entry.getValue())
                            .build()
            );
        }
        return result;
    }

    @Override
    public List<String> getAvailableDisciplines() {
        return eurekaClient.getApplications().getRegisteredApplications().stream()
                .filter(application -> !application.getInstances().isEmpty())
                .map(application -> application.getName().toLowerCase())
                .toList();
    }

    @Override
    public List<DataCreatedResponse> addNewQuestions(List<RequestAddQuestionsDTO> questions) {
        List<DataCreatedResponse> result = new ArrayList<>();

        for (RequestAddQuestionsDTO question : questions) {
            if (getAvailableDisciplines().contains(question.getDiscipline())) {
                HttpEntity<List<QuestionDTO>> request = new HttpEntity<>(question.getQuestions());
                ResponseEntity<String> response = null;
                try {
                    response = restTemplate.exchange(
                            String.format("http://%s/api", question.getDiscipline().toUpperCase()),
                            HttpMethod.POST,
                            request,
                            String.class
                    );
                } catch (RestClientException e) {
                    result.add(
                            new DataCreatedResponse(
                                    400,
                                    String.format("Неверная валидация вопросов для сервиса %s", question.getDiscipline())
                            )
                    );
                    continue;
                }
                result.add(
                        new DataCreatedResponse(
                                response.getStatusCode().value(),
                                String.format("Вопросы для %s сервиса добавлены", question.getDiscipline())
                        )
                );
            }
        }

        return result;
    }
}
