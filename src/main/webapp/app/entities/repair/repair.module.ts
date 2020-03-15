import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DescaSharedModule } from '../../shared';
import {
    RepairService,
    RepairPopupService,
    RepairComponent,
    RepairDetailComponent,
    RepairDialogComponent,
    RepairPopupComponent,
    RepairDeletePopupComponent,
    RepairDeleteDialogComponent,
    repairRoute,
    repairPopupRoute,
    RepairResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...repairRoute,
    ...repairPopupRoute,
];

@NgModule({
    imports: [
        DescaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        RepairComponent,
        RepairDetailComponent,
        RepairDialogComponent,
        RepairDeleteDialogComponent,
        RepairPopupComponent,
        RepairDeletePopupComponent,
    ],
    entryComponents: [
        RepairComponent,
        RepairDialogComponent,
        RepairPopupComponent,
        RepairDeleteDialogComponent,
        RepairDeletePopupComponent,
    ],
    providers: [
        RepairService,
        RepairPopupService,
        RepairResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DescaRepairModule {}
