import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DescaSharedModule } from '../../shared';
import {
    DescaWayService,
    DescaWayPopupService,
    DescaWayComponent,
    DescaWayDetailComponent,
    DescaWayDialogComponent,
    DescaWayPopupComponent,
    DescaWayDeletePopupComponent,
    DescaWayDeleteDialogComponent,
    descaWayRoute,
    descaWayPopupRoute,
    DescaWayResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...descaWayRoute,
    ...descaWayPopupRoute,
];

@NgModule({
    imports: [
        DescaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        DescaWayComponent,
        DescaWayDetailComponent,
        DescaWayDialogComponent,
        DescaWayDeleteDialogComponent,
        DescaWayPopupComponent,
        DescaWayDeletePopupComponent,
    ],
    entryComponents: [
        DescaWayComponent,
        DescaWayDialogComponent,
        DescaWayPopupComponent,
        DescaWayDeleteDialogComponent,
        DescaWayDeletePopupComponent,
    ],
    providers: [
        DescaWayService,
        DescaWayPopupService,
        DescaWayResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DescaDescaWayModule {}
