package mx.gob.scjn.desca.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A MainDatabase.
 */
@Entity
@Table(name = "main_database")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "maindatabase")
public class MainDatabase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_number", nullable = false)
    private String number;

    @Column(name = "intitution")
    private String intitution;

    @ManyToOne
    private MemberState memberState;

    @ManyToOne
    private JudicialProcessType judicialProcessType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public MainDatabase number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIntitution() {
        return intitution;
    }

    public MainDatabase intitution(String intitution) {
        this.intitution = intitution;
        return this;
    }

    public void setIntitution(String intitution) {
        this.intitution = intitution;
    }

    public MemberState getMemberState() {
        return memberState;
    }

    public MainDatabase memberState(MemberState memberState) {
        this.memberState = memberState;
        return this;
    }

    public void setMemberState(MemberState memberState) {
        this.memberState = memberState;
    }

    public JudicialProcessType getJudicialProcessType() {
        return judicialProcessType;
    }

    public MainDatabase judicialProcessType(JudicialProcessType judicialProcessType) {
        this.judicialProcessType = judicialProcessType;
        return this;
    }

    public void setJudicialProcessType(JudicialProcessType judicialProcessType) {
        this.judicialProcessType = judicialProcessType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MainDatabase mainDatabase = (MainDatabase) o;
        if (mainDatabase.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mainDatabase.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MainDatabase{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", intitution='" + getIntitution() + "'" +
            "}";
    }
}
