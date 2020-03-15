import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Repair } from './repair.model';
import { RepairPopupService } from './repair-popup.service';
import { RepairService } from './repair.service';

@Component({
    selector: 'jhi-repair-delete-dialog',
    templateUrl: './repair-delete-dialog.component.html'
})
export class RepairDeleteDialogComponent {

    repair: Repair;

    constructor(
        private repairService: RepairService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.repairService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'repairListModification',
                content: 'Deleted an repair'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-repair-delete-popup',
    template: ''
})
export class RepairDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private repairPopupService: RepairPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.repairPopupService
                .open(RepairDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
