package com.bytatech.repository.search;

import com.bytatech.domain.Drivo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Drivo entity.
 */
public interface DrivoSearchRepository extends ElasticsearchRepository<Drivo, Long> {
}
