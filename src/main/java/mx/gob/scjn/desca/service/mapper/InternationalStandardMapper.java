package mx.gob.scjn.desca.service.mapper;

import mx.gob.scjn.desca.domain.*;
import mx.gob.scjn.desca.service.dto.InternationalStandardDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity InternationalStandard and its DTO InternationalStandardDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InternationalStandardMapper extends EntityMapper<InternationalStandardDTO, InternationalStandard> {



    default InternationalStandard fromId(Long id) {
        if (id == null) {
            return null;
        }
        InternationalStandard internationalStandard = new InternationalStandard();
        internationalStandard.setId(id);
        return internationalStandard;
    }
}
