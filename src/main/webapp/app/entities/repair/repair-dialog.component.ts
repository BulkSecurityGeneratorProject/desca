import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Repair } from './repair.model';
import { RepairPopupService } from './repair-popup.service';
import { RepairService } from './repair.service';
import { RepairType, RepairTypeService } from '../repair-type';

@Component({
    selector: 'jhi-repair-dialog',
    templateUrl: './repair-dialog.component.html'
})
export class RepairDialogComponent implements OnInit {

    repair: Repair;
    isSaving: boolean;

    repairtypes: RepairType[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private repairService: RepairService,
        private repairTypeService: RepairTypeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.repairTypeService.query()
            .subscribe((res: HttpResponse<RepairType[]>) => { this.repairtypes = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.repair.id !== undefined) {
            this.subscribeToSaveResponse(
                this.repairService.update(this.repair));
        } else {
            this.subscribeToSaveResponse(
                this.repairService.create(this.repair));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Repair>>) {
        result.subscribe((res: HttpResponse<Repair>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Repair) {
        this.eventManager.broadcast({ name: 'repairListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackRepairTypeById(index: number, item: RepairType) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-repair-popup',
    template: ''
})
export class RepairPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private repairPopupService: RepairPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.repairPopupService
                    .open(RepairDialogComponent as Component, params['id']);
            } else {
                this.repairPopupService
                    .open(RepairDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
