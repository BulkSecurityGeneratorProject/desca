package mx.gob.scjn.desca.repository.search;

import mx.gob.scjn.desca.domain.MainDatabase;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MainDatabase entity.
 */
public interface MainDatabaseSearchRepository extends ElasticsearchRepository<MainDatabase, Long> {
}
