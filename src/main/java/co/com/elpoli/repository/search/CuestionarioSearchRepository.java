package co.com.elpoli.repository.search;

import co.com.elpoli.domain.Cuestionario;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Cuestionario entity.
 */
public interface CuestionarioSearchRepository extends ElasticsearchRepository<Cuestionario, Long> {
}
