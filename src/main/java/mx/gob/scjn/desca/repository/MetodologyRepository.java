package mx.gob.scjn.desca.repository;

import mx.gob.scjn.desca.domain.Metodology;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Metodology entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetodologyRepository extends JpaRepository<Metodology, Long>, JpaSpecificationExecutor<Metodology> {

}
