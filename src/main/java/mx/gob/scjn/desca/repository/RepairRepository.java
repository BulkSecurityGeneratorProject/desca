package mx.gob.scjn.desca.repository;

import mx.gob.scjn.desca.domain.Repair;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Repair entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RepairRepository extends JpaRepository<Repair, Long>, JpaSpecificationExecutor<Repair> {

}
