package mx.gob.scjn.desca.repository.search;

import mx.gob.scjn.desca.domain.InternationalStandard;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the InternationalStandard entity.
 */
public interface InternationalStandardSearchRepository extends ElasticsearchRepository<InternationalStandard, Long> {
}
