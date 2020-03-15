import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { MainDatabaseComponent } from './main-database.component';
import { MainDatabaseDetailComponent } from './main-database-detail.component';
import { MainDatabasePopupComponent } from './main-database-dialog.component';
import { MainDatabaseDeletePopupComponent } from './main-database-delete-dialog.component';

@Injectable()
export class MainDatabaseResolvePagingParams implements Resolve<any> {

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

export const mainDatabaseRoute: Routes = [
    {
        path: 'main-database',
        component: MainDatabaseComponent,
        resolve: {
            'pagingParams': MainDatabaseResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.mainDatabase.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'main-database/:id',
        component: MainDatabaseDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.mainDatabase.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const mainDatabasePopupRoute: Routes = [
    {
        path: 'main-database-new',
        component: MainDatabasePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.mainDatabase.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'main-database/:id/edit',
        component: MainDatabasePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.mainDatabase.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'main-database/:id/delete',
        component: MainDatabaseDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.mainDatabase.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
