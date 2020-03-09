import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { VulnerableGroupComponent } from './vulnerable-group.component';
import { VulnerableGroupDetailComponent } from './vulnerable-group-detail.component';
import { VulnerableGroupPopupComponent } from './vulnerable-group-dialog.component';
import { VulnerableGroupDeletePopupComponent } from './vulnerable-group-delete-dialog.component';

@Injectable()
export class VulnerableGroupResolvePagingParams implements Resolve<any> {

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

export const vulnerableGroupRoute: Routes = [
    {
        path: 'vulnerable-group',
        component: VulnerableGroupComponent,
        resolve: {
            'pagingParams': VulnerableGroupResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.vulnerableGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'vulnerable-group/:id',
        component: VulnerableGroupDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.vulnerableGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const vulnerableGroupPopupRoute: Routes = [
    {
        path: 'vulnerable-group-new',
        component: VulnerableGroupPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.vulnerableGroup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vulnerable-group/:id/edit',
        component: VulnerableGroupPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.vulnerableGroup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vulnerable-group/:id/delete',
        component: VulnerableGroupDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.vulnerableGroup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
