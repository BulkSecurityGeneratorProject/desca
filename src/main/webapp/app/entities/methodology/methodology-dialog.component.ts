import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Methodology } from './methodology.model';
import { MethodologyPopupService } from './methodology-popup.service';
import { MethodologyService } from './methodology.service';

@Component({
    selector: 'jhi-methodology-dialog',
    templateUrl: './methodology-dialog.component.html'
})
export class MethodologyDialogComponent implements OnInit {

    methodology: Methodology;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private methodologyService: MethodologyService,
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
        if (this.methodology.id !== undefined) {
            this.subscribeToSaveResponse(
                this.methodologyService.update(this.methodology));
        } else {
            this.subscribeToSaveResponse(
                this.methodologyService.create(this.methodology));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Methodology>>) {
        result.subscribe((res: HttpResponse<Methodology>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Methodology) {
        this.eventManager.broadcast({ name: 'methodologyListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-methodology-popup',
    template: ''
})
export class MethodologyPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private methodologyPopupService: MethodologyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.methodologyPopupService
                    .open(MethodologyDialogComponent as Component, params['id']);
            } else {
                this.methodologyPopupService
                    .open(MethodologyDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
