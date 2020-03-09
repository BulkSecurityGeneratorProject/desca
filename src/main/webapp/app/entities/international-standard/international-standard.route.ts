import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { InternationalStandardComponent } from './international-standard.component';
import { InternationalStandardDetailComponent } from './international-standard-detail.component';
import { InternationalStandardPopupComponent } from './international-standard-dialog.component';
import { InternationalStandardDeletePopupComponent } from './international-standard-delete-dialog.component';

@Injectable()
export class InternationalStandardResolvePagingParams implements Resolve<any> {

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

export const internationalStandardRoute: Routes = [
    {
        path: 'international-standard',
        component: InternationalStandardComponent,
        resolve: {
            'pagingParams': InternationalStandardResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.internationalStandard.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'international-standard/:id',
        component: InternationalStandardDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.internationalStandard.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const internationalStandardPopupRoute: Routes = [
    {
        path: 'international-standard-new',
        component: InternationalStandardPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.internationalStandard.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'international-standard/:id/edit',
        component: InternationalStandardPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.internationalStandard.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'international-standard/:id/delete',
        component: InternationalStandardDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.internationalStandard.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
