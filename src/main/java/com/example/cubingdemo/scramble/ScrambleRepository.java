package com.example.cubingdemo.scramble;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ScrambleRepository extends PagingAndSortingRepository<Scramble, Long>, CrudRepository<Scramble, Long> {

}
