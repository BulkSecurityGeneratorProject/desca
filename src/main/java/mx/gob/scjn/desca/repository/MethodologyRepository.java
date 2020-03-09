package mx.gob.scjn.desca.repository;

import mx.gob.scjn.desca.domain.Methodology;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Methodology entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MethodologyRepository extends JpaRepository<Methodology, Long>, JpaSpecificationExecutor<Methodology> {

}
