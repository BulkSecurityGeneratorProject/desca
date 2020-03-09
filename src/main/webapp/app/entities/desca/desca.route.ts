import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { DescaComponent } from './desca.component';
import { DescaDetailComponent } from './desca-detail.component';
import { DescaPopupComponent } from './desca-dialog.component';
import { DescaDeletePopupComponent } from './desca-delete-dialog.component';

@Injectable()
export class DescaResolvePagingParams implements Resolve<any> {

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

export const descaRoute: Routes = [
    {
        path: 'desca',
        component: DescaComponent,
        resolve: {
            'pagingParams': DescaResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.desca.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'desca/:id',
        component: DescaDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.desca.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const descaPopupRoute: Routes = [
    {
        path: 'desca-new',
        component: DescaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.desca.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'desca/:id/edit',
        component: DescaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.desca.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'desca/:id/delete',
        component: DescaDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.desca.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
