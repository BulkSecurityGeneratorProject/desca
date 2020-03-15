package mx.gob.scjn.desca.repository.search;

import mx.gob.scjn.desca.domain.RepairType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RepairType entity.
 */
public interface RepairTypeSearchRepository extends ElasticsearchRepository<RepairType, Long> {
}
