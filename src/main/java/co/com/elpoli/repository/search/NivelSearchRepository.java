package co.com.elpoli.repository.search;

import co.com.elpoli.domain.Nivel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Nivel entity.
 */
public interface NivelSearchRepository extends ElasticsearchRepository<Nivel, Long> {
}
