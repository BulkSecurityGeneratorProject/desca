import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Metodology } from './metodology.model';
import { MetodologyPopupService } from './metodology-popup.service';
import { MetodologyService } from './metodology.service';

@Component({
    selector: 'jhi-metodology-dialog',
    templateUrl: './metodology-dialog.component.html'
})
export class MetodologyDialogComponent implements OnInit {

    metodology: Metodology;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private metodologyService: MetodologyService,
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
        if (this.metodology.id !== undefined) {
            this.subscribeToSaveResponse(
                this.metodologyService.update(this.metodology));
        } else {
            this.subscribeToSaveResponse(
                this.metodologyService.create(this.metodology));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Metodology>>) {
        result.subscribe((res: HttpResponse<Metodology>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Metodology) {
        this.eventManager.broadcast({ name: 'metodologyListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-metodology-popup',
    template: ''
})
export class MetodologyPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private metodologyPopupService: MetodologyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.metodologyPopupService
                    .open(MetodologyDialogComponent as Component, params['id']);
            } else {
                this.metodologyPopupService
                    .open(MetodologyDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
