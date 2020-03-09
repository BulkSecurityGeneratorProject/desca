import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Desca } from './desca.model';
import { DescaPopupService } from './desca-popup.service';
import { DescaService } from './desca.service';

@Component({
    selector: 'jhi-desca-delete-dialog',
    templateUrl: './desca-delete-dialog.component.html'
})
export class DescaDeleteDialogComponent {

    desca: Desca;

    constructor(
        private descaService: DescaService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.descaService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'descaListModification',
                content: 'Deleted an desca'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-desca-delete-popup',
    template: ''
})
export class DescaDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private descaPopupService: DescaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.descaPopupService
                .open(DescaDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
