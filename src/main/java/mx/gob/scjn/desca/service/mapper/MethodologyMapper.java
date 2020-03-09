package mx.gob.scjn.desca.service.mapper;

import mx.gob.scjn.desca.domain.*;
import mx.gob.scjn.desca.service.dto.MethodologyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Methodology and its DTO MethodologyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MethodologyMapper extends EntityMapper<MethodologyDTO, Methodology> {



    default Methodology fromId(Long id) {
        if (id == null) {
            return null;
        }
        Methodology methodology = new Methodology();
        methodology.setId(id);
        return methodology;
    }
}
