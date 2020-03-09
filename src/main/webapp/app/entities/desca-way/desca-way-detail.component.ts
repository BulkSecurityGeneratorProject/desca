import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { DescaWay } from './desca-way.model';
import { DescaWayService } from './desca-way.service';

@Component({
    selector: 'jhi-desca-way-detail',
    templateUrl: './desca-way-detail.component.html'
})
export class DescaWayDetailComponent implements OnInit, OnDestroy {

    descaWay: DescaWay;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private descaWayService: DescaWayService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDescaWays();
    }

    load(id) {
        this.descaWayService.find(id)
            .subscribe((descaWayResponse: HttpResponse<DescaWay>) => {
                this.descaWay = descaWayResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDescaWays() {
        this.eventSubscriber = this.eventManager.subscribe(
            'descaWayListModification',
            (response) => this.load(this.descaWay.id)
        );
    }
}
