package co.com.elpoli.repository.search;

import co.com.elpoli.domain.Pregunta;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Pregunta entity.
 */
public interface PreguntaSearchRepository extends ElasticsearchRepository<Pregunta, Long> {
}
