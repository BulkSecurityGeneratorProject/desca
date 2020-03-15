import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { RepairType } from './repair-type.model';
import { RepairTypeService } from './repair-type.service';

@Component({
    selector: 'jhi-repair-type-detail',
    templateUrl: './repair-type-detail.component.html'
})
export class RepairTypeDetailComponent implements OnInit, OnDestroy {

    repairType: RepairType;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private repairTypeService: RepairTypeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRepairTypes();
    }

    load(id) {
        this.repairTypeService.find(id)
            .subscribe((repairTypeResponse: HttpResponse<RepairType>) => {
                this.repairType = repairTypeResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRepairTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'repairTypeListModification',
            (response) => this.load(this.repairType.id)
        );
    }
}
