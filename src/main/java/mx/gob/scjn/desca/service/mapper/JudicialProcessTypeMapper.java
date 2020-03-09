package mx.gob.scjn.desca.service.mapper;

import mx.gob.scjn.desca.domain.*;
import mx.gob.scjn.desca.service.dto.JudicialProcessTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity JudicialProcessType and its DTO JudicialProcessTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface JudicialProcessTypeMapper extends EntityMapper<JudicialProcessTypeDTO, JudicialProcessType> {



    default JudicialProcessType fromId(Long id) {
        if (id == null) {
            return null;
        }
        JudicialProcessType judicialProcessType = new JudicialProcessType();
        judicialProcessType.setId(id);
        return judicialProcessType;
    }
}
