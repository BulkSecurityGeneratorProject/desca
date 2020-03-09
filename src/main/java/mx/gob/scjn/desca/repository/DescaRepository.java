package mx.gob.scjn.desca.repository;

import mx.gob.scjn.desca.domain.Desca;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Desca entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DescaRepository extends JpaRepository<Desca, Long>, JpaSpecificationExecutor<Desca> {

}
