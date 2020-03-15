package mx.gob.scjn.desca.repository.search;

import mx.gob.scjn.desca.domain.DescaWayByC;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DescaWayByC entity.
 */
public interface DescaWayByCSearchRepository extends ElasticsearchRepository<DescaWayByC, Long> {
}
