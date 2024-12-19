package com.example.cubingdemo.scramble;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Scramble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long length = 20L;
    @Transient
    private Move[] moves;
    private String movesAsString;
    private Long time;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public String getMovesAsString() {
        return movesAsString;
    }

    public void setMovesAsString(String moves) {
        this.movesAsString = moves;
    }

    public Scramble(Long id, Long length, String movesAsString) {
        this.id = id;
        this.length = length;
        this.movesAsString = movesAsString;
    }

    public Scramble(Long length) {
        if (length != null) {
            this.length = length;
        }
        this.scramble();
    }

    private void scramble() {
        this.moves = new Move[this.length.intValue()];
        StringBuilder movesStringBuilder = new StringBuilder();
        for (int i = 0; i < this.length; i++) {
            Move move = Move.values()[(int) (Math.random() * Move.values().length)];
            Move previousMove = i > 0 ? this.moves[i - 1] : null;
            while (previousMove != null && (move.getDisplayValue().charAt(0) == previousMove.getDisplayValue().charAt(0))) {
                move = Move.values()[(int) (Math.random() * Move.values().length)];
            }
            this.moves[i] = move;
            movesStringBuilder.append(move.getDisplayValue()).append(" ");
        }
        this.movesAsString = movesStringBuilder.toString().trim();
    }

    public Scramble() {
    }

    @Override
    public String toString() {
        return "Scramble{" +
                "id=" + id +
                ", length=" + length +
                ", moves=" + movesAsString +
                '}';
    }
}
