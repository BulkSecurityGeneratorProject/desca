import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Methodology } from './methodology.model';
import { MethodologyService } from './methodology.service';

@Component({
    selector: 'jhi-methodology-detail',
    templateUrl: './methodology-detail.component.html'
})
export class MethodologyDetailComponent implements OnInit, OnDestroy {

    methodology: Methodology;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private methodologyService: MethodologyService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMethodologies();
    }

    load(id) {
        this.methodologyService.find(id)
            .subscribe((methodologyResponse: HttpResponse<Methodology>) => {
                this.methodology = methodologyResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMethodologies() {
        this.eventSubscriber = this.eventManager.subscribe(
            'methodologyListModification',
            (response) => this.load(this.methodology.id)
        );
    }
}
