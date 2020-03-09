import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MemberState } from './member-state.model';
import { MemberStatePopupService } from './member-state-popup.service';
import { MemberStateService } from './member-state.service';

@Component({
    selector: 'jhi-member-state-dialog',
    templateUrl: './member-state-dialog.component.html'
})
export class MemberStateDialogComponent implements OnInit {

    memberState: MemberState;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private memberStateService: MemberStateService,
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
        if (this.memberState.id !== undefined) {
            this.subscribeToSaveResponse(
                this.memberStateService.update(this.memberState));
        } else {
            this.subscribeToSaveResponse(
                this.memberStateService.create(this.memberState));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<MemberState>>) {
        result.subscribe((res: HttpResponse<MemberState>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: MemberState) {
        this.eventManager.broadcast({ name: 'memberStateListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-member-state-popup',
    template: ''
})
export class MemberStatePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private memberStatePopupService: MemberStatePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.memberStatePopupService
                    .open(MemberStateDialogComponent as Component, params['id']);
            } else {
                this.memberStatePopupService
                    .open(MemberStateDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
