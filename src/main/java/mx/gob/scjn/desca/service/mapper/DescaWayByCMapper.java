package mx.gob.scjn.desca.service.mapper;

import mx.gob.scjn.desca.domain.*;
import mx.gob.scjn.desca.service.dto.DescaWayByCDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DescaWayByC and its DTO DescaWayByCDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DescaWayByCMapper extends EntityMapper<DescaWayByCDTO, DescaWayByC> {



    default DescaWayByC fromId(Long id) {
        if (id == null) {
            return null;
        }
        DescaWayByC descaWayByC = new DescaWayByC();
        descaWayByC.setId(id);
        return descaWayByC;
    }
}
