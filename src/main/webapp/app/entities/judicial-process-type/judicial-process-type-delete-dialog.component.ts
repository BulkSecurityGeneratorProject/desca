import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { JudicialProcessType } from './judicial-process-type.model';
import { JudicialProcessTypePopupService } from './judicial-process-type-popup.service';
import { JudicialProcessTypeService } from './judicial-process-type.service';

@Component({
    selector: 'jhi-judicial-process-type-delete-dialog',
    templateUrl: './judicial-process-type-delete-dialog.component.html'
})
export class JudicialProcessTypeDeleteDialogComponent {

    judicialProcessType: JudicialProcessType;

    constructor(
        private judicialProcessTypeService: JudicialProcessTypeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.judicialProcessTypeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'judicialProcessTypeListModification',
                content: 'Deleted an judicialProcessType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-judicial-process-type-delete-popup',
    template: ''
})
export class JudicialProcessTypeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private judicialProcessTypePopupService: JudicialProcessTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.judicialProcessTypePopupService
                .open(JudicialProcessTypeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
