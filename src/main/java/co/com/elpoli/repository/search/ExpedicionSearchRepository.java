package co.com.elpoli.repository.search;

import co.com.elpoli.domain.Expedicion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Expedicion entity.
 */
public interface ExpedicionSearchRepository extends ElasticsearchRepository<Expedicion, Long> {
}
