import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { RepairType } from './repair-type.model';
import { RepairTypePopupService } from './repair-type-popup.service';
import { RepairTypeService } from './repair-type.service';

@Component({
    selector: 'jhi-repair-type-dialog',
    templateUrl: './repair-type-dialog.component.html'
})
export class RepairTypeDialogComponent implements OnInit {

    repairType: RepairType;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private repairTypeService: RepairTypeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.repairType.id !== undefined) {
            this.subscribeToSaveResponse(
                this.repairTypeService.update(this.repairType));
        } else {
            this.subscribeToSaveResponse(
                this.repairTypeService.create(this.repairType));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<RepairType>>) {
        result.subscribe((res: HttpResponse<RepairType>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: RepairType) {
        this.eventManager.broadcast({ name: 'repairTypeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-repair-type-popup',
    template: ''
})
export class RepairTypePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private repairTypePopupService: RepairTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.repairTypePopupService
                    .open(RepairTypeDialogComponent as Component, params['id']);
            } else {
                this.repairTypePopupService
                    .open(RepairTypeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
