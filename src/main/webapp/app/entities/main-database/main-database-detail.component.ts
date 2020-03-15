import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { MainDatabase } from './main-database.model';
import { MainDatabaseService } from './main-database.service';

@Component({
    selector: 'jhi-main-database-detail',
    templateUrl: './main-database-detail.component.html'
})
export class MainDatabaseDetailComponent implements OnInit, OnDestroy {

    mainDatabase: MainDatabase;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private mainDatabaseService: MainDatabaseService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMainDatabases();
    }

    load(id) {
        this.mainDatabaseService.find(id)
            .subscribe((mainDatabaseResponse: HttpResponse<MainDatabase>) => {
                this.mainDatabase = mainDatabaseResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMainDatabases() {
        this.eventSubscriber = this.eventManager.subscribe(
            'mainDatabaseListModification',
            (response) => this.load(this.mainDatabase.id)
        );
    }
}
