import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DescaSharedModule } from '../../shared';
import {
    VulnerableGroupService,
    VulnerableGroupPopupService,
    VulnerableGroupComponent,
    VulnerableGroupDetailComponent,
    VulnerableGroupDialogComponent,
    VulnerableGroupPopupComponent,
    VulnerableGroupDeletePopupComponent,
    VulnerableGroupDeleteDialogComponent,
    vulnerableGroupRoute,
    vulnerableGroupPopupRoute,
    VulnerableGroupResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...vulnerableGroupRoute,
    ...vulnerableGroupPopupRoute,
];

@NgModule({
    imports: [
        DescaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        VulnerableGroupComponent,
        VulnerableGroupDetailComponent,
        VulnerableGroupDialogComponent,
        VulnerableGroupDeleteDialogComponent,
        VulnerableGroupPopupComponent,
        VulnerableGroupDeletePopupComponent,
    ],
    entryComponents: [
        VulnerableGroupComponent,
        VulnerableGroupDialogComponent,
        VulnerableGroupPopupComponent,
        VulnerableGroupDeleteDialogComponent,
        VulnerableGroupDeletePopupComponent,
    ],
    providers: [
        VulnerableGroupService,
        VulnerableGroupPopupService,
        VulnerableGroupResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DescaVulnerableGroupModule {}
