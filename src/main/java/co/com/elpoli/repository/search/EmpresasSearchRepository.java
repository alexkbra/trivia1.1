package co.com.elpoli.repository.search;

import co.com.elpoli.domain.Empresas;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Empresas entity.
 */
public interface EmpresasSearchRepository extends ElasticsearchRepository<Empresas, Long> {
}
