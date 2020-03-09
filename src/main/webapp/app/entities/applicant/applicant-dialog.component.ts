import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Applicant } from './applicant.model';
import { ApplicantPopupService } from './applicant-popup.service';
import { ApplicantService } from './applicant.service';

@Component({
    selector: 'jhi-applicant-dialog',
    templateUrl: './applicant-dialog.component.html'
})
export class ApplicantDialogComponent implements OnInit {

    applicant: Applicant;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private applicantService: ApplicantService,
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
        if (this.applicant.id !== undefined) {
            this.subscribeToSaveResponse(
                this.applicantService.update(this.applicant));
        } else {
            this.subscribeToSaveResponse(
                this.applicantService.create(this.applicant));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Applicant>>) {
        result.subscribe((res: HttpResponse<Applicant>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Applicant) {
        this.eventManager.broadcast({ name: 'applicantListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-applicant-popup',
    template: ''
})
export class ApplicantPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private applicantPopupService: ApplicantPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.applicantPopupService
                    .open(ApplicantDialogComponent as Component, params['id']);
            } else {
                this.applicantPopupService
                    .open(ApplicantDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
