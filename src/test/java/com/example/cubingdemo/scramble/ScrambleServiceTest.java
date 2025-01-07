package com.example.cubingdemo.scramble;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScrambleServiceTest {

    @Mock
    private ScrambleRepository scrambleRepository;
    private ScrambleService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ScrambleService(scrambleRepository);
    }

    @Test
    void getScrambles() {
        underTest.getScrambles(null);
        verify(scrambleRepository).findAll((Pageable) null);
    }

    @Test
    void getScramble() {
        when(scrambleRepository.findById(1L)).thenReturn(Optional.of(new Scramble()));
        underTest.getScramble(1L);
        verify(scrambleRepository).findById(1L);
    }

    @Test
    void getScrambleNotFound() {
        when(scrambleRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> underTest.getScramble(1L)
        );
        assertEquals("No scramble with id 1", exception.getMessage());
        verify(scrambleRepository).findById(1L); // Verify the repository interaction
    }

    @Test
    void generateScrambleSuccessful() {
        ScrambleRequest scrambleRequest = new ScrambleRequest();
        underTest.generateScramble(scrambleRequest);
        // verify that save method was called with a Scramble object that has a length of 20 (default)
        verify(scrambleRepository).save(argThat(scramble1 -> scramble1.getLength().equals(20L)));
    }

    @Test
    void generateScrambleIllegalLength() {
        ScrambleRequest scrambleRequest = new ScrambleRequest(51L);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> underTest.generateScramble(scrambleRequest)
        );

        assertEquals("Scramble length must be less or equal to 50 and greater than 0", exception.getMessage());
        verify(scrambleRepository, never()).save(any(Scramble.class));
    }

    @Test
    void deleteScramble() {
        underTest.deleteScramble(1L);
        verify(scrambleRepository).deleteById(1L);
    }

    @Test
    void updateScramble_shouldUpdateScrambleAndSave() {
        ScrambleRequest scrambleRequest = new ScrambleRequest();
        scrambleRequest.setTime(80L); // Set a value to update

        Scramble scramble = new Scramble(); // initial time is null

        when(scrambleRepository.findById(1L)).thenReturn(Optional.of(scramble));

        // Act
        underTest.updateScramble(1L, scrambleRequest);

        // Assert
        assertEquals(80L, scramble.getTime()); // Verify the update
        verify(scrambleRepository).save(scramble); // Verify save was called
    }

}