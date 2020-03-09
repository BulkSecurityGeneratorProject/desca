import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { InternationalStandard } from './international-standard.model';
import { InternationalStandardPopupService } from './international-standard-popup.service';
import { InternationalStandardService } from './international-standard.service';

@Component({
    selector: 'jhi-international-standard-delete-dialog',
    templateUrl: './international-standard-delete-dialog.component.html'
})
export class InternationalStandardDeleteDialogComponent {

    internationalStandard: InternationalStandard;

    constructor(
        private internationalStandardService: InternationalStandardService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.internationalStandardService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'internationalStandardListModification',
                content: 'Deleted an internationalStandard'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-international-standard-delete-popup',
    template: ''
})
export class InternationalStandardDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private internationalStandardPopupService: InternationalStandardPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.internationalStandardPopupService
                .open(InternationalStandardDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
