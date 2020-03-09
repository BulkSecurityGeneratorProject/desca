package mx.gob.scjn.desca.repository;

import mx.gob.scjn.desca.domain.InternationalStandard;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the InternationalStandard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternationalStandardRepository extends JpaRepository<InternationalStandard, Long>, JpaSpecificationExecutor<InternationalStandard> {

}
