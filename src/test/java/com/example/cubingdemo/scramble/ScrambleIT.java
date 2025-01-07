package com.example.cubingdemo.scramble;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScrambleIT {

    @Autowired
    private TestRestTemplate restTemplate;

    // CustomPageImpl class is needed to deserialize the Page object returned by the endpoint
    public static class CustomPageImpl<T> extends PageImpl<T> {
        @JsonCreator
        public CustomPageImpl(@JsonProperty("content") List<T> content,
                              @JsonProperty("number") int number,
                              @JsonProperty("size") int size,
                              @JsonProperty("totalElements") Long totalElements,
                              @JsonProperty("pageable") JsonNode pageable,
                              @JsonProperty("last") boolean last,
                              @JsonProperty("totalPages") int totalPages,
                              @JsonProperty("sort") JsonNode sort,
                              @JsonProperty("numberOfElements") int numberOfElements) {
            super(content, PageRequest.of(number, size), totalElements);
        }
        public CustomPageImpl(List<T> content) {
            super(content);
        }
    }

    @Test
    public void getScrambles() {
        String url = "/scrambles";
        // Test the endpoint while keeping in mind that it returns a Page object
        ResponseEntity<CustomPageImpl<Scramble>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<CustomPageImpl<Scramble>>() {}
        );
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        // ensure that 5 scrambles are returned
        assertThat(responseEntity.getBody().getContent()).hasSize(5);
        // test some values of the first scramble
        assertThat(responseEntity.getBody().getContent().get(0).getLength()).isEqualTo(30);
        assertThat(responseEntity.getBody().getContent().get(0).getMovesAsString()).isEqualTo("R U F D'");
    }

    @Test
    public void getScramble() {
        String url = "/scrambles/{id}";
        Scramble scramble = restTemplate.getForObject(url, Scramble.class, 1);
        assertThat(scramble.getId()).isEqualTo(1);
        assertThat(scramble.getLength()).isEqualTo(20);
        assertThat(scramble.getMovesAsString()).isEqualTo("R U R' U'");
    }

    @Test
    public void createScramble() {
        String url = "/scrambles";
        ScrambleRequest scrambleRequest = new ScrambleRequest(25L);
        URI newAccountLocation = restTemplate.postForLocation(url, scrambleRequest);

        Scramble scramble = restTemplate.getForObject(newAccountLocation, Scramble.class);
        assertThat(scramble.getId()).isEqualTo(6L);
        assertThat(scramble.getLength()).isEqualTo(25L);
        assertThat(scramble.getMovesAsString()).isInstanceOf(String.class);
    }

    @Test
    public void updateScramble() {
        String url = "/scrambles/{id}";
        ScrambleRequest scrambleRequest = new ScrambleRequest();
        scrambleRequest.setTime(10L);
        restTemplate.put(url, scrambleRequest, 1);
        Scramble scramble = restTemplate.getForObject(url, Scramble.class, 1);
        assertThat(scramble.getId()).isEqualTo(1L);
        assertThat(scramble.getLength()).isEqualTo(20L);
        assertThat(scramble.getTime()).isEqualTo(10L);
        assertThat(scramble.getMovesAsString()).isInstanceOf(String.class);
    }

    @Test
    public void deleteScramble() {
        String url = "/scrambles/{id}";
        restTemplate.delete(url, 1);
        ResponseEntity<Scramble> response = restTemplate.getForEntity(url, Scramble.class, 1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
