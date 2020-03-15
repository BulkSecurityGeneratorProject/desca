import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MainDatabase } from './main-database.model';
import { MainDatabasePopupService } from './main-database-popup.service';
import { MainDatabaseService } from './main-database.service';

@Component({
    selector: 'jhi-main-database-dialog',
    templateUrl: './main-database-dialog.component.html'
})
export class MainDatabaseDialogComponent implements OnInit {

    mainDatabase: MainDatabase;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private mainDatabaseService: MainDatabaseService,
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
        if (this.mainDatabase.id !== undefined) {
            this.subscribeToSaveResponse(
                this.mainDatabaseService.update(this.mainDatabase));
        } else {
            this.subscribeToSaveResponse(
                this.mainDatabaseService.create(this.mainDatabase));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<MainDatabase>>) {
        result.subscribe((res: HttpResponse<MainDatabase>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: MainDatabase) {
        this.eventManager.broadcast({ name: 'mainDatabaseListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-main-database-popup',
    template: ''
})
export class MainDatabasePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mainDatabasePopupService: MainDatabasePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.mainDatabasePopupService
                    .open(MainDatabaseDialogComponent as Component, params['id']);
            } else {
                this.mainDatabasePopupService
                    .open(MainDatabaseDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
