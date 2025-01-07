package com.example.cubingdemo.scramble;

public class ScrambleRequest {

    public ScrambleRequest() {
    }

    public ScrambleRequest(Long length) {
        this.length = length;
    }

    private Long length;
    private Long time;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }
}