import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { InternationalStandar } from './international-standar.model';
import { InternationalStandarPopupService } from './international-standar-popup.service';
import { InternationalStandarService } from './international-standar.service';

@Component({
    selector: 'jhi-international-standar-dialog',
    templateUrl: './international-standar-dialog.component.html'
})
export class InternationalStandarDialogComponent implements OnInit {

    internationalStandar: InternationalStandar;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private internationalStandarService: InternationalStandarService,
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
        if (this.internationalStandar.id !== undefined) {
            this.subscribeToSaveResponse(
                this.internationalStandarService.update(this.internationalStandar));
        } else {
            this.subscribeToSaveResponse(
                this.internationalStandarService.create(this.internationalStandar));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<InternationalStandar>>) {
        result.subscribe((res: HttpResponse<InternationalStandar>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: InternationalStandar) {
        this.eventManager.broadcast({ name: 'internationalStandarListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-international-standar-popup',
    template: ''
})
export class InternationalStandarPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private internationalStandarPopupService: InternationalStandarPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.internationalStandarPopupService
                    .open(InternationalStandarDialogComponent as Component, params['id']);
            } else {
                this.internationalStandarPopupService
                    .open(InternationalStandarDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
