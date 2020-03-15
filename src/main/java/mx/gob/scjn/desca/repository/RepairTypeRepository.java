package mx.gob.scjn.desca.repository;

import mx.gob.scjn.desca.domain.RepairType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RepairType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RepairTypeRepository extends JpaRepository<RepairType, Long>, JpaSpecificationExecutor<RepairType> {

}
