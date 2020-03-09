import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { JudicialProcessType } from './judicial-process-type.model';
import { JudicialProcessTypePopupService } from './judicial-process-type-popup.service';
import { JudicialProcessTypeService } from './judicial-process-type.service';

@Component({
    selector: 'jhi-judicial-process-type-dialog',
    templateUrl: './judicial-process-type-dialog.component.html'
})
export class JudicialProcessTypeDialogComponent implements OnInit {

    judicialProcessType: JudicialProcessType;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private judicialProcessTypeService: JudicialProcessTypeService,
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
        if (this.judicialProcessType.id !== undefined) {
            this.subscribeToSaveResponse(
                this.judicialProcessTypeService.update(this.judicialProcessType));
        } else {
            this.subscribeToSaveResponse(
                this.judicialProcessTypeService.create(this.judicialProcessType));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<JudicialProcessType>>) {
        result.subscribe((res: HttpResponse<JudicialProcessType>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: JudicialProcessType) {
        this.eventManager.broadcast({ name: 'judicialProcessTypeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-judicial-process-type-popup',
    template: ''
})
export class JudicialProcessTypePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private judicialProcessTypePopupService: JudicialProcessTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.judicialProcessTypePopupService
                    .open(JudicialProcessTypeDialogComponent as Component, params['id']);
            } else {
                this.judicialProcessTypePopupService
                    .open(JudicialProcessTypeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
