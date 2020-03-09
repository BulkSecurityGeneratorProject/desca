import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Desca } from './desca.model';
import { DescaPopupService } from './desca-popup.service';
import { DescaService } from './desca.service';

@Component({
    selector: 'jhi-desca-dialog',
    templateUrl: './desca-dialog.component.html'
})
export class DescaDialogComponent implements OnInit {

    desca: Desca;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private descaService: DescaService,
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
        if (this.desca.id !== undefined) {
            this.subscribeToSaveResponse(
                this.descaService.update(this.desca));
        } else {
            this.subscribeToSaveResponse(
                this.descaService.create(this.desca));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Desca>>) {
        result.subscribe((res: HttpResponse<Desca>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Desca) {
        this.eventManager.broadcast({ name: 'descaListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-desca-popup',
    template: ''
})
export class DescaPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private descaPopupService: DescaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.descaPopupService
                    .open(DescaDialogComponent as Component, params['id']);
            } else {
                this.descaPopupService
                    .open(DescaDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
