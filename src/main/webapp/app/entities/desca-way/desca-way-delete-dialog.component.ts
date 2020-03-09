import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { DescaWay } from './desca-way.model';
import { DescaWayPopupService } from './desca-way-popup.service';
import { DescaWayService } from './desca-way.service';

@Component({
    selector: 'jhi-desca-way-delete-dialog',
    templateUrl: './desca-way-delete-dialog.component.html'
})
export class DescaWayDeleteDialogComponent {

    descaWay: DescaWay;

    constructor(
        private descaWayService: DescaWayService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.descaWayService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'descaWayListModification',
                content: 'Deleted an descaWay'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-desca-way-delete-popup',
    template: ''
})
export class DescaWayDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private descaWayPopupService: DescaWayPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.descaWayPopupService
                .open(DescaWayDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
