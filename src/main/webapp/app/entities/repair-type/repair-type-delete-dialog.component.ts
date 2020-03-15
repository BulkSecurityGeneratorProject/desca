import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { RepairType } from './repair-type.model';
import { RepairTypePopupService } from './repair-type-popup.service';
import { RepairTypeService } from './repair-type.service';

@Component({
    selector: 'jhi-repair-type-delete-dialog',
    templateUrl: './repair-type-delete-dialog.component.html'
})
export class RepairTypeDeleteDialogComponent {

    repairType: RepairType;

    constructor(
        private repairTypeService: RepairTypeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.repairTypeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'repairTypeListModification',
                content: 'Deleted an repairType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-repair-type-delete-popup',
    template: ''
})
export class RepairTypeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private repairTypePopupService: RepairTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.repairTypePopupService
                .open(RepairTypeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
