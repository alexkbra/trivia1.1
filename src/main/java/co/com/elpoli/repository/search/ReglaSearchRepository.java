package co.com.elpoli.repository.search;

import co.com.elpoli.domain.Regla;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Regla entity.
 */
public interface ReglaSearchRepository extends ElasticsearchRepository<Regla, Long> {
}
