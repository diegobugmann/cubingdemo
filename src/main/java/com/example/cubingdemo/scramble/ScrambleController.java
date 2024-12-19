package com.example.cubingdemo.scramble;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "/scrambles")
public class ScrambleController {

    private ScrambleService scrambleService;

    @Autowired
    public ScrambleController(ScrambleService scrambleService) {
        this.scrambleService = scrambleService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Scramble> getScrambles(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, value = 20) Pageable p) {
        return this.scrambleService.getScrambles(p);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Scramble getScramble(@PathVariable Long id) {
        return this.scrambleService.getScramble(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Scramble updateScramble(@PathVariable Long id, @RequestBody ScrambleRequest scrambleRequest) {
        return this.scrambleService.updateScramble(id, scrambleRequest);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createScramble(@RequestBody(required = false) ScrambleRequest scrambleRequest) {
        Scramble scramble = this.scrambleService.generateScramble(scrambleRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(scramble.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteScramble(@PathVariable Long id) {
        this.scrambleService.deleteScramble(id);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalArgumentException.class)
    public void handleNotFound(Exception ex) {
        // return empty 404
    }

}
