package com.bytatech.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of DrivoSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DrivoSearchRepositoryMockConfiguration {

    @MockBean
    private DrivoSearchRepository mockDrivoSearchRepository;

}
