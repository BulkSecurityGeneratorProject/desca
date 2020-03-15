import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { RepairComponent } from './repair.component';
import { RepairDetailComponent } from './repair-detail.component';
import { RepairPopupComponent } from './repair-dialog.component';
import { RepairDeletePopupComponent } from './repair-delete-dialog.component';

@Injectable()
export class RepairResolvePagingParams implements Resolve<any> {

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

export const repairRoute: Routes = [
    {
        path: 'repair',
        component: RepairComponent,
        resolve: {
            'pagingParams': RepairResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.repair.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'repair/:id',
        component: RepairDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.repair.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const repairPopupRoute: Routes = [
    {
        path: 'repair-new',
        component: RepairPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.repair.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'repair/:id/edit',
        component: RepairPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.repair.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'repair/:id/delete',
        component: RepairDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.repair.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
