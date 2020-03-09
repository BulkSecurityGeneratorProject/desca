package mx.gob.scjn.desca.repository;

import mx.gob.scjn.desca.domain.InternationalStandar;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the InternationalStandar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternationalStandarRepository extends JpaRepository<InternationalStandar, Long>, JpaSpecificationExecutor<InternationalStandar> {

}
