package mx.gob.scjn.desca.service.mapper;

import mx.gob.scjn.desca.domain.*;
import mx.gob.scjn.desca.service.dto.RepairDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Repair and its DTO RepairDTO.
 */
@Mapper(componentModel = "spring", uses = {RepairTypeMapper.class})
public interface RepairMapper extends EntityMapper<RepairDTO, Repair> {

    @Mapping(source = "repairType.id", target = "repairTypeId")
    RepairDTO toDto(Repair repair);

    @Mapping(source = "repairTypeId", target = "repairType")
    Repair toEntity(RepairDTO repairDTO);

    default Repair fromId(Long id) {
        if (id == null) {
            return null;
        }
        Repair repair = new Repair();
        repair.setId(id);
        return repair;
    }
}
