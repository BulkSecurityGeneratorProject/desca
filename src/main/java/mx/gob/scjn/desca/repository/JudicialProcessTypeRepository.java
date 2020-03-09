package mx.gob.scjn.desca.repository;

import mx.gob.scjn.desca.domain.JudicialProcessType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the JudicialProcessType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JudicialProcessTypeRepository extends JpaRepository<JudicialProcessType, Long>, JpaSpecificationExecutor<JudicialProcessType> {

}
