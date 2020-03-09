import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Applicant } from './applicant.model';
import { ApplicantService } from './applicant.service';

@Component({
    selector: 'jhi-applicant-detail',
    templateUrl: './applicant-detail.component.html'
})
export class ApplicantDetailComponent implements OnInit, OnDestroy {

    applicant: Applicant;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private applicantService: ApplicantService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInApplicants();
    }

    load(id) {
        this.applicantService.find(id)
            .subscribe((applicantResponse: HttpResponse<Applicant>) => {
                this.applicant = applicantResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInApplicants() {
        this.eventSubscriber = this.eventManager.subscribe(
            'applicantListModification',
            (response) => this.load(this.applicant.id)
        );
    }
}
