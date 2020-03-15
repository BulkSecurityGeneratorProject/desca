package mx.gob.scjn.desca.repository.search;

import mx.gob.scjn.desca.domain.DescaWay;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DescaWay entity.
 */
public interface DescaWaySearchRepository extends ElasticsearchRepository<DescaWay, Long> {
}
