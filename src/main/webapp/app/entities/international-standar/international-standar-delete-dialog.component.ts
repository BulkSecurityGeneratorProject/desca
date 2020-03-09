import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { InternationalStandar } from './international-standar.model';
import { InternationalStandarPopupService } from './international-standar-popup.service';
import { InternationalStandarService } from './international-standar.service';

@Component({
    selector: 'jhi-international-standar-delete-dialog',
    templateUrl: './international-standar-delete-dialog.component.html'
})
export class InternationalStandarDeleteDialogComponent {

    internationalStandar: InternationalStandar;

    constructor(
        private internationalStandarService: InternationalStandarService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.internationalStandarService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'internationalStandarListModification',
                content: 'Deleted an internationalStandar'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-international-standar-delete-popup',
    template: ''
})
export class InternationalStandarDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private internationalStandarPopupService: InternationalStandarPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.internationalStandarPopupService
                .open(InternationalStandarDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
