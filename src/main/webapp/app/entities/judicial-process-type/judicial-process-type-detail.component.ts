import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { JudicialProcessType } from './judicial-process-type.model';
import { JudicialProcessTypeService } from './judicial-process-type.service';

@Component({
    selector: 'jhi-judicial-process-type-detail',
    templateUrl: './judicial-process-type-detail.component.html'
})
export class JudicialProcessTypeDetailComponent implements OnInit, OnDestroy {

    judicialProcessType: JudicialProcessType;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private judicialProcessTypeService: JudicialProcessTypeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInJudicialProcessTypes();
    }

    load(id) {
        this.judicialProcessTypeService.find(id)
            .subscribe((judicialProcessTypeResponse: HttpResponse<JudicialProcessType>) => {
                this.judicialProcessType = judicialProcessTypeResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInJudicialProcessTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'judicialProcessTypeListModification',
            (response) => this.load(this.judicialProcessType.id)
        );
    }
}
