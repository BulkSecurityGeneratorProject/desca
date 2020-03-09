package mx.gob.scjn.desca.service.mapper;

import mx.gob.scjn.desca.domain.*;
import mx.gob.scjn.desca.service.dto.DescaWayDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DescaWay and its DTO DescaWayDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DescaWayMapper extends EntityMapper<DescaWayDTO, DescaWay> {



    default DescaWay fromId(Long id) {
        if (id == null) {
            return null;
        }
        DescaWay descaWay = new DescaWay();
        descaWay.setId(id);
        return descaWay;
    }
}
