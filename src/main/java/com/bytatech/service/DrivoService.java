package com.bytatech.service;

import com.bytatech.service.dto.DrivoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Drivo.
 */
public interface DrivoService {

    /**
     * Save a drivo.
     *
     * @param drivoDTO the entity to save
     * @return the persisted entity
     */
    DrivoDTO save(DrivoDTO drivoDTO);

    /**
     * Get all the drivos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DrivoDTO> findAll(Pageable pageable);


    /**
     * Get the "id" drivo.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DrivoDTO> findOne(Long id);

    /**
     * Delete the "id" drivo.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the drivo corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DrivoDTO> search(String query, Pageable pageable);
}
