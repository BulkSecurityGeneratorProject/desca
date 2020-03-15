package mx.gob.scjn.desca.service.mapper;

import mx.gob.scjn.desca.domain.*;
import mx.gob.scjn.desca.service.dto.InstitutionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Institution and its DTO InstitutionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InstitutionMapper extends EntityMapper<InstitutionDTO, Institution> {



    default Institution fromId(Long id) {
        if (id == null) {
            return null;
        }
        Institution institution = new Institution();
        institution.setId(id);
        return institution;
    }
}
