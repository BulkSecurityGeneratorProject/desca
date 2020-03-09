import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { DescaWayComponent } from './desca-way.component';
import { DescaWayDetailComponent } from './desca-way-detail.component';
import { DescaWayPopupComponent } from './desca-way-dialog.component';
import { DescaWayDeletePopupComponent } from './desca-way-delete-dialog.component';

@Injectable()
export class DescaWayResolvePagingParams implements Resolve<any> {

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

export const descaWayRoute: Routes = [
    {
        path: 'desca-way',
        component: DescaWayComponent,
        resolve: {
            'pagingParams': DescaWayResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.descaWay.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'desca-way/:id',
        component: DescaWayDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.descaWay.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const descaWayPopupRoute: Routes = [
    {
        path: 'desca-way-new',
        component: DescaWayPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.descaWay.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'desca-way/:id/edit',
        component: DescaWayPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.descaWay.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'desca-way/:id/delete',
        component: DescaWayDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.descaWay.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
