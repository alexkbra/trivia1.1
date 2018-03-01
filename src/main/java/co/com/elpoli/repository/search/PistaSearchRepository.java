package co.com.elpoli.repository.search;

import co.com.elpoli.domain.Pista;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Pista entity.
 */
public interface PistaSearchRepository extends ElasticsearchRepository<Pista, Long> {
}
