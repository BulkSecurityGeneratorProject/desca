package mx.gob.scjn.desca.repository.search;

import mx.gob.scjn.desca.domain.Methodology;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Methodology entity.
 */
public interface MethodologySearchRepository extends ElasticsearchRepository<Methodology, Long> {
}
