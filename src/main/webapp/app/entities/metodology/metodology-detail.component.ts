import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Metodology } from './metodology.model';
import { MetodologyService } from './metodology.service';

@Component({
    selector: 'jhi-metodology-detail',
    templateUrl: './metodology-detail.component.html'
})
export class MetodologyDetailComponent implements OnInit, OnDestroy {

    metodology: Metodology;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private metodologyService: MetodologyService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMetodologies();
    }

    load(id) {
        this.metodologyService.find(id)
            .subscribe((metodologyResponse: HttpResponse<Metodology>) => {
                this.metodology = metodologyResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMetodologies() {
        this.eventSubscriber = this.eventManager.subscribe(
            'metodologyListModification',
            (response) => this.load(this.metodology.id)
        );
    }
}
