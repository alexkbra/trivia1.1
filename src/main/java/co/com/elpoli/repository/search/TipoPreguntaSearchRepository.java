package co.com.elpoli.repository.search;

import co.com.elpoli.domain.TipoPregunta;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TipoPregunta entity.
 */
public interface TipoPreguntaSearchRepository extends ElasticsearchRepository<TipoPregunta, Long> {
}
