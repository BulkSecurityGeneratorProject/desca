import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Metodology } from './metodology.model';
import { MetodologyPopupService } from './metodology-popup.service';
import { MetodologyService } from './metodology.service';

@Component({
    selector: 'jhi-metodology-delete-dialog',
    templateUrl: './metodology-delete-dialog.component.html'
})
export class MetodologyDeleteDialogComponent {

    metodology: Metodology;

    constructor(
        private metodologyService: MetodologyService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.metodologyService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'metodologyListModification',
                content: 'Deleted an metodology'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-metodology-delete-popup',
    template: ''
})
export class MetodologyDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private metodologyPopupService: MetodologyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.metodologyPopupService
                .open(MetodologyDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
