import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { InternationalStandard } from './international-standard.model';
import { InternationalStandardService } from './international-standard.service';

@Component({
    selector: 'jhi-international-standard-detail',
    templateUrl: './international-standard-detail.component.html'
})
export class InternationalStandardDetailComponent implements OnInit, OnDestroy {

    internationalStandard: InternationalStandard;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private internationalStandardService: InternationalStandardService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInInternationalStandards();
    }

    load(id) {
        this.internationalStandardService.find(id)
            .subscribe((internationalStandardResponse: HttpResponse<InternationalStandard>) => {
                this.internationalStandard = internationalStandardResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInInternationalStandards() {
        this.eventSubscriber = this.eventManager.subscribe(
            'internationalStandardListModification',
            (response) => this.load(this.internationalStandard.id)
        );
    }
}
