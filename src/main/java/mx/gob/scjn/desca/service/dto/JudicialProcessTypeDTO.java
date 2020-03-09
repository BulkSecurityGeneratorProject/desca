package mx.gob.scjn.desca.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the JudicialProcessType entity.
 */
public class JudicialProcessTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Boolean status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JudicialProcessTypeDTO judicialProcessTypeDTO = (JudicialProcessTypeDTO) o;
        if(judicialProcessTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), judicialProcessTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JudicialProcessTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + isStatus() + "'" +
            "}";
    }
}
