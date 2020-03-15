package mx.gob.scjn.desca.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the MainDatabase entity.
 */
public class MainDatabaseDTO implements Serializable {

    private Long id;

    @NotNull
    private String number;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MainDatabaseDTO mainDatabaseDTO = (MainDatabaseDTO) o;
        if(mainDatabaseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mainDatabaseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MainDatabaseDTO{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            "}";
    }
}
