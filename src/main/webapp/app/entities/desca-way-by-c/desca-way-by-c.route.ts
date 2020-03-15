import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { DescaWayByCComponent } from './desca-way-by-c.component';
import { DescaWayByCDetailComponent } from './desca-way-by-c-detail.component';
import { DescaWayByCPopupComponent } from './desca-way-by-c-dialog.component';
import { DescaWayByCDeletePopupComponent } from './desca-way-by-c-delete-dialog.component';

@Injectable()
export class DescaWayByCResolvePagingParams implements Resolve<any> {

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

export const descaWayByCRoute: Routes = [
    {
        path: 'desca-way-by-c',
        component: DescaWayByCComponent,
        resolve: {
            'pagingParams': DescaWayByCResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.descaWayByC.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'desca-way-by-c/:id',
        component: DescaWayByCDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.descaWayByC.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const descaWayByCPopupRoute: Routes = [
    {
        path: 'desca-way-by-c-new',
        component: DescaWayByCPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.descaWayByC.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'desca-way-by-c/:id/edit',
        component: DescaWayByCPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.descaWayByC.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'desca-way-by-c/:id/delete',
        component: DescaWayByCDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.descaWayByC.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
