package mx.gob.scjn.desca.repository.search;

import mx.gob.scjn.desca.domain.MemberState;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MemberState entity.
 */
public interface MemberStateSearchRepository extends ElasticsearchRepository<MemberState, Long> {
}
