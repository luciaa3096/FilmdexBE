package org.filmdex.filmdexbe.repositories;

import org.filmdex.filmdexbe.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> { }