import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { MemberStateComponent } from './member-state.component';
import { MemberStateDetailComponent } from './member-state-detail.component';
import { MemberStatePopupComponent } from './member-state-dialog.component';
import { MemberStateDeletePopupComponent } from './member-state-delete-dialog.component';

@Injectable()
export class MemberStateResolvePagingParams implements Resolve<any> {

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

export const memberStateRoute: Routes = [
    {
        path: 'member-state',
        component: MemberStateComponent,
        resolve: {
            'pagingParams': MemberStateResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.memberState.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'member-state/:id',
        component: MemberStateDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.memberState.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const memberStatePopupRoute: Routes = [
    {
        path: 'member-state-new',
        component: MemberStatePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.memberState.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'member-state/:id/edit',
        component: MemberStatePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.memberState.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'member-state/:id/delete',
        component: MemberStateDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.memberState.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
