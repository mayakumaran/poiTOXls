package com.bytatech.repository;

import com.bytatech.domain.Drivo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Drivo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DrivoRepository extends JpaRepository<Drivo, Long> {

}
