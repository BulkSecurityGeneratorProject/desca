import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DescaSharedModule } from '../../shared';
import {
    RepairTypeService,
    RepairTypePopupService,
    RepairTypeComponent,
    RepairTypeDetailComponent,
    RepairTypeDialogComponent,
    RepairTypePopupComponent,
    RepairTypeDeletePopupComponent,
    RepairTypeDeleteDialogComponent,
    repairTypeRoute,
    repairTypePopupRoute,
    RepairTypeResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...repairTypeRoute,
    ...repairTypePopupRoute,
];

@NgModule({
    imports: [
        DescaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        RepairTypeComponent,
        RepairTypeDetailComponent,
        RepairTypeDialogComponent,
        RepairTypeDeleteDialogComponent,
        RepairTypePopupComponent,
        RepairTypeDeletePopupComponent,
    ],
    entryComponents: [
        RepairTypeComponent,
        RepairTypeDialogComponent,
        RepairTypePopupComponent,
        RepairTypeDeleteDialogComponent,
        RepairTypeDeletePopupComponent,
    ],
    providers: [
        RepairTypeService,
        RepairTypePopupService,
        RepairTypeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DescaRepairTypeModule {}
