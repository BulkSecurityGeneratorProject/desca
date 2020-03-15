import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Repair } from './repair.model';
import { RepairService } from './repair.service';

@Component({
    selector: 'jhi-repair-detail',
    templateUrl: './repair-detail.component.html'
})
export class RepairDetailComponent implements OnInit, OnDestroy {

    repair: Repair;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private repairService: RepairService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRepairs();
    }

    load(id) {
        this.repairService.find(id)
            .subscribe((repairResponse: HttpResponse<Repair>) => {
                this.repair = repairResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRepairs() {
        this.eventSubscriber = this.eventManager.subscribe(
            'repairListModification',
            (response) => this.load(this.repair.id)
        );
    }
}
