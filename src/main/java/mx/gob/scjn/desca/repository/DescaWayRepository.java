package mx.gob.scjn.desca.repository;

import mx.gob.scjn.desca.domain.DescaWay;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the DescaWay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DescaWayRepository extends JpaRepository<DescaWay, Long>, JpaSpecificationExecutor<DescaWay> {

}
