import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ApplicantComponent } from './applicant.component';
import { ApplicantDetailComponent } from './applicant-detail.component';
import { ApplicantPopupComponent } from './applicant-dialog.component';
import { ApplicantDeletePopupComponent } from './applicant-delete-dialog.component';

@Injectable()
export class ApplicantResolvePagingParams implements Resolve<any> {

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

export const applicantRoute: Routes = [
    {
        path: 'applicant',
        component: ApplicantComponent,
        resolve: {
            'pagingParams': ApplicantResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.applicant.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'applicant/:id',
        component: ApplicantDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.applicant.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const applicantPopupRoute: Routes = [
    {
        path: 'applicant-new',
        component: ApplicantPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.applicant.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'applicant/:id/edit',
        component: ApplicantPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.applicant.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'applicant/:id/delete',
        component: ApplicantDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'descaApp.applicant.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
