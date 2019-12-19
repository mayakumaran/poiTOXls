package com.bytatech.service.impl;

import com.bytatech.service.DrivoService;
import com.bytatech.domain.Drivo;
import com.bytatech.repository.DrivoRepository;
import com.bytatech.repository.search.DrivoSearchRepository;
import com.bytatech.service.dto.DrivoDTO;
import com.bytatech.service.mapper.DrivoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Drivo.
 */
@Service
@Transactional
public class DrivoServiceImpl implements DrivoService {

    private final Logger log = LoggerFactory.getLogger(DrivoServiceImpl.class);

    private final DrivoRepository drivoRepository;

    private final DrivoMapper drivoMapper;

    private final DrivoSearchRepository drivoSearchRepository;

    public DrivoServiceImpl(DrivoRepository drivoRepository, DrivoMapper drivoMapper, DrivoSearchRepository drivoSearchRepository) {
        this.drivoRepository = drivoRepository;
        this.drivoMapper = drivoMapper;
        this.drivoSearchRepository = drivoSearchRepository;
    }

    /**
     * Save a drivo.
     *
     * @param drivoDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DrivoDTO save(DrivoDTO drivoDTO) {
        log.debug("Request to save Drivo : {}", drivoDTO);
        Drivo drivo = drivoMapper.toEntity(drivoDTO);
        drivo = drivoRepository.save(drivo);
        DrivoDTO result = drivoMapper.toDto(drivo);
        drivoSearchRepository.save(drivo);
        return result;
    }

    /**
     * Get all the drivos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DrivoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Drivos");
        return drivoRepository.findAll(pageable)
            .map(drivoMapper::toDto);
    }


    /**
     * Get one drivo by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DrivoDTO> findOne(Long id) {
        log.debug("Request to get Drivo : {}", id);
        return drivoRepository.findById(id)
            .map(drivoMapper::toDto);
    }

    /**
     * Delete the drivo by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Drivo : {}", id);        drivoRepository.deleteById(id);
        drivoSearchRepository.deleteById(id);
    }

    /**
     * Search for the drivo corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DrivoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Drivos for query {}", query);
        return drivoSearchRepository.search(queryStringQuery(query), pageable)
            .map(drivoMapper::toDto);
    }
}
