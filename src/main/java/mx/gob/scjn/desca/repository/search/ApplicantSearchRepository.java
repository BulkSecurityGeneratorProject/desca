package mx.gob.scjn.desca.repository.search;

import mx.gob.scjn.desca.domain.Applicant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Applicant entity.
 */
public interface ApplicantSearchRepository extends ElasticsearchRepository<Applicant, Long> {
}
