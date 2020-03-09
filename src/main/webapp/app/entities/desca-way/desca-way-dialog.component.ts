import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { DescaWay } from './desca-way.model';
import { DescaWayPopupService } from './desca-way-popup.service';
import { DescaWayService } from './desca-way.service';

@Component({
    selector: 'jhi-desca-way-dialog',
    templateUrl: './desca-way-dialog.component.html'
})
export class DescaWayDialogComponent implements OnInit {

    descaWay: DescaWay;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private descaWayService: DescaWayService,
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
        if (this.descaWay.id !== undefined) {
            this.subscribeToSaveResponse(
                this.descaWayService.update(this.descaWay));
        } else {
            this.subscribeToSaveResponse(
                this.descaWayService.create(this.descaWay));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<DescaWay>>) {
        result.subscribe((res: HttpResponse<DescaWay>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: DescaWay) {
        this.eventManager.broadcast({ name: 'descaWayListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-desca-way-popup',
    template: ''
})
export class DescaWayPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private descaWayPopupService: DescaWayPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.descaWayPopupService
                    .open(DescaWayDialogComponent as Component, params['id']);
            } else {
                this.descaWayPopupService
                    .open(DescaWayDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
