package co.com.elpoli.repository.search;

import co.com.elpoli.domain.Publicidad;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Publicidad entity.
 */
public interface PublicidadSearchRepository extends ElasticsearchRepository<Publicidad, Long> {
}
