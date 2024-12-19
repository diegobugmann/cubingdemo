package com.example.cubingdemo.scramble;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ScrambleService {

    private ScrambleRepository scrambleRepository;

    public ScrambleService(ScrambleRepository scrambleRepository) {
        this.scrambleRepository = scrambleRepository;
    }

    public Iterable<Scramble> getScrambles(Pageable p) {
        return this.scrambleRepository.findAll(p);
    }

    public Scramble generateScramble(ScrambleRequest scrambleRequest) {
        Long length = null;
        if (scrambleRequest != null && scrambleRequest.getLength() != null) {
            length = scrambleRequest.getLength();
        }
        if (length != null && (length > 50 || length <= 0)) {
            throw new IllegalArgumentException("Scramble length must be less or equal to 50 and greater than 0");
        }
        Scramble scramble = new Scramble(length);
        return this.scrambleRepository.save(scramble);
    }

    public Scramble getScramble(Long id) {
        Scramble scramble = this.scrambleRepository.findById(id).orElse(null);
        if (scramble == null) {
            throw new IllegalArgumentException("No scramble with id " + id);
        }
        return scramble;
    }

    public void deleteScramble(Long id) {
        this.scrambleRepository.deleteById(id);
    }

    public Scramble updateScramble(Long id, ScrambleRequest scrambleRequest) {
        Scramble scramble = this.getScramble(id);
        if (scrambleRequest.getTime() != null) {
            scramble.setTime(scrambleRequest.getTime());
        }
        return this.scrambleRepository.save(scramble);
    }
}
