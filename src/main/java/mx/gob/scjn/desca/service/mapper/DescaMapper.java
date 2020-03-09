package mx.gob.scjn.desca.service.mapper;

import mx.gob.scjn.desca.domain.*;
import mx.gob.scjn.desca.service.dto.DescaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Desca and its DTO DescaDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DescaMapper extends EntityMapper<DescaDTO, Desca> {



    default Desca fromId(Long id) {
        if (id == null) {
            return null;
        }
        Desca desca = new Desca();
        desca.setId(id);
        return desca;
    }
}
