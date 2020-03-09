import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { JudicialProcessTypeComponent } from './judicial-process-type.component';
import { JudicialProcessTypeDetailComponent } from './judicial-process-type-detail.component';
import { JudicialProcessTypePopupComponent } from './judicial-process-type-dialog.component';
import { JudicialProcessTypeDeletePopupComponent } from './judicial-process-type-delete-dialog.component';

@Injectable()
export class JudicialProcessTypeResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const judicialProcessTypeRoute: Routes = [
    {
        path: 'judicial-process-type',
        component: JudicialProcessTypeComponent,
        resolve: {
            'pagingParams': JudicialProcessTypeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.judicialProcessType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'judicial-process-type/:id',
        component: JudicialProcessTypeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.judicialProcessType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const judicialProcessTypePopupRoute: Routes = [
    {
        path: 'judicial-process-type-new',
        component: JudicialProcessTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.judicialProcessType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'judicial-process-type/:id/edit',
        component: JudicialProcessTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.judicialProcessType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'judicial-process-type/:id/delete',
        component: JudicialProcessTypeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.judicialProcessType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
