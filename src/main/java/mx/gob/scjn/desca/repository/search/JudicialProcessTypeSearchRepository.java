package mx.gob.scjn.desca.repository.search;

import mx.gob.scjn.desca.domain.JudicialProcessType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the JudicialProcessType entity.
 */
public interface JudicialProcessTypeSearchRepository extends ElasticsearchRepository<JudicialProcessType, Long> {
}
