package com.example.cubingdemo.scramble;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ScrambleRepositoryTest {

    @Autowired
    private ScrambleRepository scrambleRepository;

    @AfterEach
    void tearDown() {
        scrambleRepository.deleteAll();
    }

    @Test
    void itShouldSaveScramble() {
        // given
        Scramble scramble = new Scramble();
        Scramble savedScramble = scrambleRepository.save(scramble);

        // when
        Scramble expectedScramble = scrambleRepository.findById(savedScramble.getId()).get();

        // then
        assertThat(expectedScramble).isEqualTo(savedScramble);
    }

}