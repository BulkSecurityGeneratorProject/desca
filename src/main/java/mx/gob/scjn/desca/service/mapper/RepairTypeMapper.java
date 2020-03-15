package mx.gob.scjn.desca.service.mapper;

import mx.gob.scjn.desca.domain.*;
import mx.gob.scjn.desca.service.dto.RepairTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RepairType and its DTO RepairTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RepairTypeMapper extends EntityMapper<RepairTypeDTO, RepairType> {



    default RepairType fromId(Long id) {
        if (id == null) {
            return null;
        }
        RepairType repairType = new RepairType();
        repairType.setId(id);
        return repairType;
    }
}
