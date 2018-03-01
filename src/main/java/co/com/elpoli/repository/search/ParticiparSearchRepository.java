package co.com.elpoli.repository.search;

import co.com.elpoli.domain.Participar;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Participar entity.
 */
public interface ParticiparSearchRepository extends ElasticsearchRepository<Participar, Long> {
}
