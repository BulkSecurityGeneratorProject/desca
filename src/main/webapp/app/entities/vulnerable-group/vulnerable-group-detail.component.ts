import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { VulnerableGroup } from './vulnerable-group.model';
import { VulnerableGroupService } from './vulnerable-group.service';

@Component({
    selector: 'jhi-vulnerable-group-detail',
    templateUrl: './vulnerable-group-detail.component.html'
})
export class VulnerableGroupDetailComponent implements OnInit, OnDestroy {

    vulnerableGroup: VulnerableGroup;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private vulnerableGroupService: VulnerableGroupService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInVulnerableGroups();
    }

    load(id) {
        this.vulnerableGroupService.find(id)
            .subscribe((vulnerableGroupResponse: HttpResponse<VulnerableGroup>) => {
                this.vulnerableGroup = vulnerableGroupResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInVulnerableGroups() {
        this.eventSubscriber = this.eventManager.subscribe(
            'vulnerableGroupListModification',
            (response) => this.load(this.vulnerableGroup.id)
        );
    }
}
