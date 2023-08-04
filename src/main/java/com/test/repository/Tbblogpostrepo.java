package com.test.repository;

import com.test.entity.Tbblogpost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface Tbblogpostrepo extends JpaRepository<Tbblogpost, Integer>, PagingAndSortingRepository<Tbblogpost, Integer> {
}
