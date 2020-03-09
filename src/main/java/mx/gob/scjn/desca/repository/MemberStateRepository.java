package mx.gob.scjn.desca.repository;

import mx.gob.scjn.desca.domain.MemberState;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MemberState entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemberStateRepository extends JpaRepository<MemberState, Long>, JpaSpecificationExecutor<MemberState> {

}
