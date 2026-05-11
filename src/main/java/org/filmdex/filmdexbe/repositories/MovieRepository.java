package org.filmdex.filmdexbe.repositories;

import org.filmdex.filmdexbe.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> { }