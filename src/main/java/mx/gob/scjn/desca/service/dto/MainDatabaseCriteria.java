package mx.gob.scjn.desca.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the MainDatabase entity. This class is used in MainDatabaseResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /main-databases?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MainDatabaseCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter number;

    private StringFilter intitution;

    private LongFilter memberStateId;

    private LongFilter judicialProcessTypeId;

    public MainDatabaseCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNumber() {
        return number;
    }

    public void setNumber(StringFilter number) {
        this.number = number;
    }

    public StringFilter getIntitution() {
        return intitution;
    }

    public void setIntitution(StringFilter intitution) {
        this.intitution = intitution;
    }

    public LongFilter getMemberStateId() {
        return memberStateId;
    }

    public void setMemberStateId(LongFilter memberStateId) {
        this.memberStateId = memberStateId;
    }

    public LongFilter getJudicialProcessTypeId() {
        return judicialProcessTypeId;
    }

    public void setJudicialProcessTypeId(LongFilter judicialProcessTypeId) {
        this.judicialProcessTypeId = judicialProcessTypeId;
    }

    @Override
    public String toString() {
        return "MainDatabaseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (number != null ? "number=" + number + ", " : "") +
                (intitution != null ? "intitution=" + intitution + ", " : "") +
                (memberStateId != null ? "memberStateId=" + memberStateId + ", " : "") +
                (judicialProcessTypeId != null ? "judicialProcessTypeId=" + judicialProcessTypeId + ", " : "") +
            "}";
    }

}
