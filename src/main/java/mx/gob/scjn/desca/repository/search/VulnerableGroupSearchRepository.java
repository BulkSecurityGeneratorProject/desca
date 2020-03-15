package mx.gob.scjn.desca.repository.search;

import mx.gob.scjn.desca.domain.VulnerableGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the VulnerableGroup entity.
 */
public interface VulnerableGroupSearchRepository extends ElasticsearchRepository<VulnerableGroup, Long> {
}
