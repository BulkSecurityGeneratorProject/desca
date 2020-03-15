import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { DescaWayByC } from './desca-way-by-c.model';
import { DescaWayByCPopupService } from './desca-way-by-c-popup.service';
import { DescaWayByCService } from './desca-way-by-c.service';

@Component({
    selector: 'jhi-desca-way-by-c-dialog',
    templateUrl: './desca-way-by-c-dialog.component.html'
})
export class DescaWayByCDialogComponent implements OnInit {

    descaWayByC: DescaWayByC;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private descaWayByCService: DescaWayByCService,
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
        if (this.descaWayByC.id !== undefined) {
            this.subscribeToSaveResponse(
                this.descaWayByCService.update(this.descaWayByC));
        } else {
            this.subscribeToSaveResponse(
                this.descaWayByCService.create(this.descaWayByC));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<DescaWayByC>>) {
        result.subscribe((res: HttpResponse<DescaWayByC>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: DescaWayByC) {
        this.eventManager.broadcast({ name: 'descaWayByCListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-desca-way-by-c-popup',
    template: ''
})
export class DescaWayByCPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private descaWayByCPopupService: DescaWayByCPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.descaWayByCPopupService
                    .open(DescaWayByCDialogComponent as Component, params['id']);
            } else {
                this.descaWayByCPopupService
                    .open(DescaWayByCDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
