package mx.gob.scjn.desca.repository.search;

import mx.gob.scjn.desca.domain.Repair;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Repair entity.
 */
public interface RepairSearchRepository extends ElasticsearchRepository<Repair, Long> {
}
