import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { MethodologyComponent } from './methodology.component';
import { MethodologyDetailComponent } from './methodology-detail.component';
import { MethodologyPopupComponent } from './methodology-dialog.component';
import { MethodologyDeletePopupComponent } from './methodology-delete-dialog.component';

@Injectable()
export class MethodologyResolvePagingParams implements Resolve<any> {

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

export const methodologyRoute: Routes = [
    {
        path: 'methodology',
        component: MethodologyComponent,
        resolve: {
            'pagingParams': MethodologyResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.methodology.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'methodology/:id',
        component: MethodologyDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.methodology.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const methodologyPopupRoute: Routes = [
    {
        path: 'methodology-new',
        component: MethodologyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.methodology.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'methodology/:id/edit',
        component: MethodologyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.methodology.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'methodology/:id/delete',
        component: MethodologyDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.methodology.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
