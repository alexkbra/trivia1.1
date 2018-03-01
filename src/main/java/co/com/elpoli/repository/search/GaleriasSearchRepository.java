package co.com.elpoli.repository.search;

import co.com.elpoli.domain.Galerias;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Galerias entity.
 */
public interface GaleriasSearchRepository extends ElasticsearchRepository<Galerias, Long> {
}
