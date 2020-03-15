import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { RepairTypeComponent } from './repair-type.component';
import { RepairTypeDetailComponent } from './repair-type-detail.component';
import { RepairTypePopupComponent } from './repair-type-dialog.component';
import { RepairTypeDeletePopupComponent } from './repair-type-delete-dialog.component';

@Injectable()
export class RepairTypeResolvePagingParams implements Resolve<any> {

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

export const repairTypeRoute: Routes = [
    {
        path: 'repair-type',
        component: RepairTypeComponent,
        resolve: {
            'pagingParams': RepairTypeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.repairType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'repair-type/:id',
        component: RepairTypeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.repairType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const repairTypePopupRoute: Routes = [
    {
        path: 'repair-type-new',
        component: RepairTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.repairType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'repair-type/:id/edit',
        component: RepairTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.repairType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'repair-type/:id/delete',
        component: RepairTypeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.repairType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
