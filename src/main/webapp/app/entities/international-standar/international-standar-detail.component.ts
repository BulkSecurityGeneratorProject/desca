import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { InternationalStandar } from './international-standar.model';
import { InternationalStandarService } from './international-standar.service';

@Component({
    selector: 'jhi-international-standar-detail',
    templateUrl: './international-standar-detail.component.html'
})
export class InternationalStandarDetailComponent implements OnInit, OnDestroy {

    internationalStandar: InternationalStandar;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private internationalStandarService: InternationalStandarService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInInternationalStandars();
    }

    load(id) {
        this.internationalStandarService.find(id)
            .subscribe((internationalStandarResponse: HttpResponse<InternationalStandar>) => {
                this.internationalStandar = internationalStandarResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInInternationalStandars() {
        this.eventSubscriber = this.eventManager.subscribe(
            'internationalStandarListModification',
            (response) => this.load(this.internationalStandar.id)
        );
    }
}
