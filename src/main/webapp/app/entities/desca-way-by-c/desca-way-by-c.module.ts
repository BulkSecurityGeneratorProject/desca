import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DescaSharedModule } from '../../shared';
import {
    DescaWayByCService,
    DescaWayByCPopupService,
    DescaWayByCComponent,
    DescaWayByCDetailComponent,
    DescaWayByCDialogComponent,
    DescaWayByCPopupComponent,
    DescaWayByCDeletePopupComponent,
    DescaWayByCDeleteDialogComponent,
    descaWayByCRoute,
    descaWayByCPopupRoute,
    DescaWayByCResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...descaWayByCRoute,
    ...descaWayByCPopupRoute,
];

@NgModule({
    imports: [
        DescaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        DescaWayByCComponent,
        DescaWayByCDetailComponent,
        DescaWayByCDialogComponent,
        DescaWayByCDeleteDialogComponent,
        DescaWayByCPopupComponent,
        DescaWayByCDeletePopupComponent,
    ],
    entryComponents: [
        DescaWayByCComponent,
        DescaWayByCDialogComponent,
        DescaWayByCPopupComponent,
        DescaWayByCDeleteDialogComponent,
        DescaWayByCDeletePopupComponent,
    ],
    providers: [
        DescaWayByCService,
        DescaWayByCPopupService,
        DescaWayByCResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DescaDescaWayByCModule {}
