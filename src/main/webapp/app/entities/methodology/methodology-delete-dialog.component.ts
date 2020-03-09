import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Methodology } from './methodology.model';
import { MethodologyPopupService } from './methodology-popup.service';
import { MethodologyService } from './methodology.service';

@Component({
    selector: 'jhi-methodology-delete-dialog',
    templateUrl: './methodology-delete-dialog.component.html'
})
export class MethodologyDeleteDialogComponent {

    methodology: Methodology;

    constructor(
        private methodologyService: MethodologyService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.methodologyService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'methodologyListModification',
                content: 'Deleted an methodology'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-methodology-delete-popup',
    template: ''
})
export class MethodologyDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private methodologyPopupService: MethodologyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.methodologyPopupService
                .open(MethodologyDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
