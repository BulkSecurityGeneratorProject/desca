package mx.gob.scjn.desca.repository;

import mx.gob.scjn.desca.domain.MainDatabase;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MainDatabase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MainDatabaseRepository extends JpaRepository<MainDatabase, Long>, JpaSpecificationExecutor<MainDatabase> {

}
