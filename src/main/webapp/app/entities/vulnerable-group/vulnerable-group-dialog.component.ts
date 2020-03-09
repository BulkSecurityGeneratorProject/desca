import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { VulnerableGroup } from './vulnerable-group.model';
import { VulnerableGroupPopupService } from './vulnerable-group-popup.service';
import { VulnerableGroupService } from './vulnerable-group.service';

@Component({
    selector: 'jhi-vulnerable-group-dialog',
    templateUrl: './vulnerable-group-dialog.component.html'
})
export class VulnerableGroupDialogComponent implements OnInit {

    vulnerableGroup: VulnerableGroup;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private vulnerableGroupService: VulnerableGroupService,
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
        if (this.vulnerableGroup.id !== undefined) {
            this.subscribeToSaveResponse(
                this.vulnerableGroupService.update(this.vulnerableGroup));
        } else {
            this.subscribeToSaveResponse(
                this.vulnerableGroupService.create(this.vulnerableGroup));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<VulnerableGroup>>) {
        result.subscribe((res: HttpResponse<VulnerableGroup>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: VulnerableGroup) {
        this.eventManager.broadcast({ name: 'vulnerableGroupListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-vulnerable-group-popup',
    template: ''
})
export class VulnerableGroupPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private vulnerableGroupPopupService: VulnerableGroupPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.vulnerableGroupPopupService
                    .open(VulnerableGroupDialogComponent as Component, params['id']);
            } else {
                this.vulnerableGroupPopupService
                    .open(VulnerableGroupDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
