import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { MetodologyComponent } from './metodology.component';
import { MetodologyDetailComponent } from './metodology-detail.component';
import { MetodologyPopupComponent } from './metodology-dialog.component';
import { MetodologyDeletePopupComponent } from './metodology-delete-dialog.component';

@Injectable()
export class MetodologyResolvePagingParams implements Resolve<any> {

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

export const metodologyRoute: Routes = [
    {
        path: 'metodology',
        component: MetodologyComponent,
        resolve: {
            'pagingParams': MetodologyResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.metodology.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'metodology/:id',
        component: MetodologyDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.metodology.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const metodologyPopupRoute: Routes = [
    {
        path: 'metodology-new',
        component: MetodologyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.metodology.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'metodology/:id/edit',
        component: MetodologyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.metodology.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'metodology/:id/delete',
        component: MetodologyDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.metodology.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
