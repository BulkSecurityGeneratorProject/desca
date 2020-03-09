import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DescaSharedModule } from '../../shared';
import {
    ApplicantService,
    ApplicantPopupService,
    ApplicantComponent,
    ApplicantDetailComponent,
    ApplicantDialogComponent,
    ApplicantPopupComponent,
    ApplicantDeletePopupComponent,
    ApplicantDeleteDialogComponent,
    applicantRoute,
    applicantPopupRoute,
    ApplicantResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...applicantRoute,
    ...applicantPopupRoute,
];

@NgModule({
    imports: [
        DescaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ApplicantComponent,
        ApplicantDetailComponent,
        ApplicantDialogComponent,
        ApplicantDeleteDialogComponent,
        ApplicantPopupComponent,
        ApplicantDeletePopupComponent,
    ],
    entryComponents: [
        ApplicantComponent,
        ApplicantDialogComponent,
        ApplicantPopupComponent,
        ApplicantDeleteDialogComponent,
        ApplicantDeletePopupComponent,
    ],
    providers: [
        ApplicantService,
        ApplicantPopupService,
        ApplicantResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DescaApplicantModule {}
