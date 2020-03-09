package mx.gob.scjn.desca.service.mapper;

import mx.gob.scjn.desca.domain.*;
import mx.gob.scjn.desca.service.dto.MemberStateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MemberState and its DTO MemberStateDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MemberStateMapper extends EntityMapper<MemberStateDTO, MemberState> {



    default MemberState fromId(Long id) {
        if (id == null) {
            return null;
        }
        MemberState memberState = new MemberState();
        memberState.setId(id);
        return memberState;
    }
}
