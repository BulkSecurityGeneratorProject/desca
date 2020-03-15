import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MainDatabase } from './main-database.model';
import { MainDatabasePopupService } from './main-database-popup.service';
import { MainDatabaseService } from './main-database.service';

@Component({
    selector: 'jhi-main-database-delete-dialog',
    templateUrl: './main-database-delete-dialog.component.html'
})
export class MainDatabaseDeleteDialogComponent {

    mainDatabase: MainDatabase;

    constructor(
        private mainDatabaseService: MainDatabaseService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.mainDatabaseService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'mainDatabaseListModification',
                content: 'Deleted an mainDatabase'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-main-database-delete-popup',
    template: ''
})
export class MainDatabaseDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mainDatabasePopupService: MainDatabasePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.mainDatabasePopupService
                .open(MainDatabaseDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
