import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { InternationalStandarComponent } from './international-standar.component';
import { InternationalStandarDetailComponent } from './international-standar-detail.component';
import { InternationalStandarPopupComponent } from './international-standar-dialog.component';
import { InternationalStandarDeletePopupComponent } from './international-standar-delete-dialog.component';

@Injectable()
export class InternationalStandarResolvePagingParams implements Resolve<any> {

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

export const internationalStandarRoute: Routes = [
    {
        path: 'international-standar',
        component: InternationalStandarComponent,
        resolve: {
            'pagingParams': InternationalStandarResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.internationalStandar.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'international-standar/:id',
        component: InternationalStandarDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.internationalStandar.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const internationalStandarPopupRoute: Routes = [
    {
        path: 'international-standar-new',
        component: InternationalStandarPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.internationalStandar.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'international-standar/:id/edit',
        component: InternationalStandarPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.internationalStandar.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'international-standar/:id/delete',
        component: InternationalStandarDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.internationalStandar.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
