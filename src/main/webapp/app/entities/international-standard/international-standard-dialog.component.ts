import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { InternationalStandard } from './international-standard.model';
import { InternationalStandardPopupService } from './international-standard-popup.service';
import { InternationalStandardService } from './international-standard.service';

@Component({
    selector: 'jhi-international-standard-dialog',
    templateUrl: './international-standard-dialog.component.html'
})
export class InternationalStandardDialogComponent implements OnInit {

    internationalStandard: InternationalStandard;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private internationalStandardService: InternationalStandardService,
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
        if (this.internationalStandard.id !== undefined) {
            this.subscribeToSaveResponse(
                this.internationalStandardService.update(this.internationalStandard));
        } else {
            this.subscribeToSaveResponse(
                this.internationalStandardService.create(this.internationalStandard));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<InternationalStandard>>) {
        result.subscribe((res: HttpResponse<InternationalStandard>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: InternationalStandard) {
        this.eventManager.broadcast({ name: 'internationalStandardListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-international-standard-popup',
    template: ''
})
export class InternationalStandardPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private internationalStandardPopupService: InternationalStandardPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.internationalStandardPopupService
                    .open(InternationalStandardDialogComponent as Component, params['id']);
            } else {
                this.internationalStandardPopupService
                    .open(InternationalStandardDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
