import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DescaSharedModule } from '../../shared';
import {
    MemberStateService,
    MemberStatePopupService,
    MemberStateComponent,
    MemberStateDetailComponent,
    MemberStateDialogComponent,
    MemberStatePopupComponent,
    MemberStateDeletePopupComponent,
    MemberStateDeleteDialogComponent,
    memberStateRoute,
    memberStatePopupRoute,
    MemberStateResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...memberStateRoute,
    ...memberStatePopupRoute,
];

@NgModule({
    imports: [
        DescaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        MemberStateComponent,
        MemberStateDetailComponent,
        MemberStateDialogComponent,
        MemberStateDeleteDialogComponent,
        MemberStatePopupComponent,
        MemberStateDeletePopupComponent,
    ],
    entryComponents: [
        MemberStateComponent,
        MemberStateDialogComponent,
        MemberStatePopupComponent,
        MemberStateDeleteDialogComponent,
        MemberStateDeletePopupComponent,
    ],
    providers: [
        MemberStateService,
        MemberStatePopupService,
        MemberStateResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DescaMemberStateModule {}
