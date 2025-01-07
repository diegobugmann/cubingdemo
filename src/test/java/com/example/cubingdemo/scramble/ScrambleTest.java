package com.example.cubingdemo.scramble;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

class ScrambleTest {

    @Test
    public void testScrambleGeneration() {
        Scramble scramble = new Scramble(20L);

        // Verify length
        assertThat(scramble.getLength().longValue()).isEqualTo(20L);

        // Verify movesAsString is not null or empty
        assertThat(scramble.getMovesAsString()).isNotNull();
        assertThat(scramble.getMovesAsString()).isNotEmpty();

        // Verify no consecutive moves share the same base character
        String[] moves = scramble.getMovesAsString().split(" ");
        for (int i = 1; i < moves.length; i++) {
            assertThat(moves[i - 1].charAt(0)).isNotEqualTo(moves[i].charAt(0));
        }
    }

}