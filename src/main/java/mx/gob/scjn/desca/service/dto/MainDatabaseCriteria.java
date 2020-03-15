package mx.gob.scjn.desca.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



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

    private LocalDateFilter resolutionDate;

    private LongFilter memberStateId;

    private LongFilter judicialProcessTypeId;

    private LongFilter descaWayByCId;

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

    public LocalDateFilter getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(LocalDateFilter resolutionDate) {
        this.resolutionDate = resolutionDate;
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

    public LongFilter getDescaWayByCId() {
        return descaWayByCId;
    }

    public void setDescaWayByCId(LongFilter descaWayByCId) {
        this.descaWayByCId = descaWayByCId;
    }

    @Override
    public String toString() {
        return "MainDatabaseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (number != null ? "number=" + number + ", " : "") +
                (intitution != null ? "intitution=" + intitution + ", " : "") +
                (resolutionDate != null ? "resolutionDate=" + resolutionDate + ", " : "") +
                (memberStateId != null ? "memberStateId=" + memberStateId + ", " : "") +
                (judicialProcessTypeId != null ? "judicialProcessTypeId=" + judicialProcessTypeId + ", " : "") +
                (descaWayByCId != null ? "descaWayByCId=" + descaWayByCId + ", " : "") +
            "}";
    }

}
