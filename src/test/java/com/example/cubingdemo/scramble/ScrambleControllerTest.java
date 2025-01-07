package com.example.cubingdemo.scramble;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScrambleController.class)
class ScrambleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScrambleService scrambleService;

    @Test
    void getScrambles() throws Exception {
        // arrange
        given(scrambleService.getScrambles(any()))
                .willReturn(new PageImpl<>(Arrays.asList(new Scramble(20L), new Scramble(25L)), PageRequest.of(0, 20), 2L));

        // act and assert
        mockMvc.perform(get("/scrambles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].length").value(20))
                .andExpect(jsonPath("$.content[0].movesAsString").isString())
                .andExpect(jsonPath("$.content[1].length").value(25))
                .andExpect(jsonPath("$.content[1].movesAsString").isString());

        // verify
        verify(scrambleService).getScrambles(any());
    }

    @Test
    void getScramble() throws Exception {
        // arrange
        given(scrambleService.getScramble(anyLong()))
                .willReturn(new Scramble(20L));

        // act and assert
        mockMvc.perform(get("/scrambles/0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("length").value(20))
                .andExpect(jsonPath("movesAsString").isString());
    }

    @Test
    void getScrambleFails() throws Exception {
        // arrange
        given(scrambleService.getScramble(anyLong()))
                .willThrow(new IllegalArgumentException("No scramble with id 0"));

        // act and assert
        mockMvc.perform(get("/scrambles/999"))
                .andExpect(status().isNotFound());

        // verify
        verify(scrambleService).getScramble(anyLong());
    }

    @Test
    void updateScramble() throws Exception {
        // arrange
        Long newLength = 80L;
        ScrambleRequest scrambleRequest = new ScrambleRequest(newLength);
        given(scrambleService.updateScramble(anyLong(), any(ScrambleRequest.class)))
                .willReturn(new Scramble(newLength));

        // act and assert
        mockMvc.perform(put("/scrambles/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(scrambleRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("length").value(80))
                .andExpect(jsonPath("movesAsString").isString());

        // verify
        verify(scrambleService).updateScramble(anyLong(), any(ScrambleRequest.class));
    }

    @Test
    void createScramble() throws Exception {
        // arrange
        ScrambleRequest scrambleRequest = new ScrambleRequest(20L);
        Scramble scramble = new Scramble(20L);
        scramble.setId(99L);
        given(scrambleService.generateScramble(any(ScrambleRequest.class)))
                .willReturn(scramble);
        given(scrambleService.getScramble(99L))
                .willReturn(scramble);

        // act and assert
        mockMvc.perform(post("/scrambles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(scrambleRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/scrambles/" + scramble.getId()));
        mockMvc.perform(get("/scrambles/" + scramble.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("length").value(20))
                .andExpect(jsonPath("movesAsString").isString());

        // verify
        verify(scrambleService).generateScramble(any(ScrambleRequest.class));
        verify(scrambleService).getScramble(99L);
    }

    @Test
    void createScrumbleFail() throws Exception {
        // arrange
        ScrambleRequest scrambleRequest = new ScrambleRequest(51L);
        given(scrambleService.generateScramble(any(ScrambleRequest.class)))
                .willThrow(new IllegalArgumentException("Scramble length must be less or equal to 50 and greater than 0"));

        // act and assert
        mockMvc.perform(post("/scrambles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(scrambleRequest)))
                .andExpect(status().isNotFound());

        // verify
        verify(scrambleService).generateScramble(any(ScrambleRequest.class));
    }

    @Test
    void deleteScramble() throws Exception {
        // arrange
        Long id = 1L;

        // act
        mockMvc.perform(delete("/scrambles/" + id))
                .andExpect(status().isNoContent());

        // verify
        verify(scrambleService).deleteScramble(id);
    }

    protected static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}