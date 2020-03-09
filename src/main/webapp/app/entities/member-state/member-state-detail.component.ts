import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { MemberState } from './member-state.model';
import { MemberStateService } from './member-state.service';

@Component({
    selector: 'jhi-member-state-detail',
    templateUrl: './member-state-detail.component.html'
})
export class MemberStateDetailComponent implements OnInit, OnDestroy {

    memberState: MemberState;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private memberStateService: MemberStateService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMemberStates();
    }

    load(id) {
        this.memberStateService.find(id)
            .subscribe((memberStateResponse: HttpResponse<MemberState>) => {
                this.memberState = memberStateResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMemberStates() {
        this.eventSubscriber = this.eventManager.subscribe(
            'memberStateListModification',
            (response) => this.load(this.memberState.id)
        );
    }
}
