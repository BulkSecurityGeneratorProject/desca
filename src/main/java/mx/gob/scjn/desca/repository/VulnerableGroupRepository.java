package mx.gob.scjn.desca.repository;

import mx.gob.scjn.desca.domain.VulnerableGroup;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the VulnerableGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VulnerableGroupRepository extends JpaRepository<VulnerableGroup, Long>, JpaSpecificationExecutor<VulnerableGroup> {

}
