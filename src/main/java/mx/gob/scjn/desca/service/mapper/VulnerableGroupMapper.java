package mx.gob.scjn.desca.service.mapper;

import mx.gob.scjn.desca.domain.*;
import mx.gob.scjn.desca.service.dto.VulnerableGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity VulnerableGroup and its DTO VulnerableGroupDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VulnerableGroupMapper extends EntityMapper<VulnerableGroupDTO, VulnerableGroup> {



    default VulnerableGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        VulnerableGroup vulnerableGroup = new VulnerableGroup();
        vulnerableGroup.setId(id);
        return vulnerableGroup;
    }
}
