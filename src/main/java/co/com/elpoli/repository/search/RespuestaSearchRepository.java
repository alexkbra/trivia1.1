package co.com.elpoli.repository.search;

import co.com.elpoli.domain.Respuesta;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Respuesta entity.
 */
public interface RespuestaSearchRepository extends ElasticsearchRepository<Respuesta, Long> {
}
