package mx.gob.scjn.desca.service.mapper;

import mx.gob.scjn.desca.domain.*;
import mx.gob.scjn.desca.service.dto.ApplicantDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Applicant and its DTO ApplicantDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ApplicantMapper extends EntityMapper<ApplicantDTO, Applicant> {



    default Applicant fromId(Long id) {
        if (id == null) {
            return null;
        }
        Applicant applicant = new Applicant();
        applicant.setId(id);
        return applicant;
    }
}
