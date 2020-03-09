import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { VulnerableGroup } from './vulnerable-group.model';
import { VulnerableGroupPopupService } from './vulnerable-group-popup.service';
import { VulnerableGroupService } from './vulnerable-group.service';

@Component({
    selector: 'jhi-vulnerable-group-delete-dialog',
    templateUrl: './vulnerable-group-delete-dialog.component.html'
})
export class VulnerableGroupDeleteDialogComponent {

    vulnerableGroup: VulnerableGroup;

    constructor(
        private vulnerableGroupService: VulnerableGroupService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.vulnerableGroupService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'vulnerableGroupListModification',
                content: 'Deleted an vulnerableGroup'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-vulnerable-group-delete-popup',
    template: ''
})
export class VulnerableGroupDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private vulnerableGroupPopupService: VulnerableGroupPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.vulnerableGroupPopupService
                .open(VulnerableGroupDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
