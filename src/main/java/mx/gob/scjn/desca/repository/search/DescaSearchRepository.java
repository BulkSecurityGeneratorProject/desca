package mx.gob.scjn.desca.repository.search;

import mx.gob.scjn.desca.domain.Desca;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Desca entity.
 */
public interface DescaSearchRepository extends ElasticsearchRepository<Desca, Long> {
}
