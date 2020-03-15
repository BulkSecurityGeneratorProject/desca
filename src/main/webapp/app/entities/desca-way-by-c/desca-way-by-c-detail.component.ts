import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { DescaWayByC } from './desca-way-by-c.model';
import { DescaWayByCService } from './desca-way-by-c.service';

@Component({
    selector: 'jhi-desca-way-by-c-detail',
    templateUrl: './desca-way-by-c-detail.component.html'
})
export class DescaWayByCDetailComponent implements OnInit, OnDestroy {

    descaWayByC: DescaWayByC;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private descaWayByCService: DescaWayByCService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDescaWayByCS();
    }

    load(id) {
        this.descaWayByCService.find(id)
            .subscribe((descaWayByCResponse: HttpResponse<DescaWayByC>) => {
                this.descaWayByC = descaWayByCResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDescaWayByCS() {
        this.eventSubscriber = this.eventManager.subscribe(
            'descaWayByCListModification',
            (response) => this.load(this.descaWayByC.id)
        );
    }
}
