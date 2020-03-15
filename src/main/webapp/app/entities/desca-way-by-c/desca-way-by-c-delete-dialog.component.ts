import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { DescaWayByC } from './desca-way-by-c.model';
import { DescaWayByCPopupService } from './desca-way-by-c-popup.service';
import { DescaWayByCService } from './desca-way-by-c.service';

@Component({
    selector: 'jhi-desca-way-by-c-delete-dialog',
    templateUrl: './desca-way-by-c-delete-dialog.component.html'
})
export class DescaWayByCDeleteDialogComponent {

    descaWayByC: DescaWayByC;

    constructor(
        private descaWayByCService: DescaWayByCService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.descaWayByCService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'descaWayByCListModification',
                content: 'Deleted an descaWayByC'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-desca-way-by-c-delete-popup',
    template: ''
})
export class DescaWayByCDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private descaWayByCPopupService: DescaWayByCPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.descaWayByCPopupService
                .open(DescaWayByCDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
