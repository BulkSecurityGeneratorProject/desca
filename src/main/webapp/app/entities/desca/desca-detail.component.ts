import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Desca } from './desca.model';
import { DescaService } from './desca.service';

@Component({
    selector: 'jhi-desca-detail',
    templateUrl: './desca-detail.component.html'
})
export class DescaDetailComponent implements OnInit, OnDestroy {

    desca: Desca;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private descaService: DescaService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDescas();
    }

    load(id) {
        this.descaService.find(id)
            .subscribe((descaResponse: HttpResponse<Desca>) => {
                this.desca = descaResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDescas() {
        this.eventSubscriber = this.eventManager.subscribe(
            'descaListModification',
            (response) => this.load(this.desca.id)
        );
    }
}
