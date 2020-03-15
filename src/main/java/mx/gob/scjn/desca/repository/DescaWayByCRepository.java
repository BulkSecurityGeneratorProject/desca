package mx.gob.scjn.desca.repository;

import mx.gob.scjn.desca.domain.DescaWayByC;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the DescaWayByC entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DescaWayByCRepository extends JpaRepository<DescaWayByC, Long>, JpaSpecificationExecutor<DescaWayByC> {

}
