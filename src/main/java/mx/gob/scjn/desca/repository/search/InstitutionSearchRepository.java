package mx.gob.scjn.desca.repository.search;

import mx.gob.scjn.desca.domain.Institution;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Institution entity.
 */
public interface InstitutionSearchRepository extends ElasticsearchRepository<Institution, Long> {
}
