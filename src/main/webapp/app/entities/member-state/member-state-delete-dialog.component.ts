import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MemberState } from './member-state.model';
import { MemberStatePopupService } from './member-state-popup.service';
import { MemberStateService } from './member-state.service';

@Component({
    selector: 'jhi-member-state-delete-dialog',
    templateUrl: './member-state-delete-dialog.component.html'
})
export class MemberStateDeleteDialogComponent {

    memberState: MemberState;

    constructor(
        private memberStateService: MemberStateService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.memberStateService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'memberStateListModification',
                content: 'Deleted an memberState'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-member-state-delete-popup',
    template: ''
})
export class MemberStateDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private memberStatePopupService: MemberStatePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.memberStatePopupService
                .open(MemberStateDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
