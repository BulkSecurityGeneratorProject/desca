import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DescaSharedModule } from '../../shared';
import {
    InternationalStandardService,
    InternationalStandardPopupService,
    InternationalStandardComponent,
    InternationalStandardDetailComponent,
    InternationalStandardDialogComponent,
    InternationalStandardPopupComponent,
    InternationalStandardDeletePopupComponent,
    InternationalStandardDeleteDialogComponent,
    internationalStandardRoute,
    internationalStandardPopupRoute,
    InternationalStandardResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...internationalStandardRoute,
    ...internationalStandardPopupRoute,
];

@NgModule({
    imports: [
        DescaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        InternationalStandardComponent,
        InternationalStandardDetailComponent,
        InternationalStandardDialogComponent,
        InternationalStandardDeleteDialogComponent,
        InternationalStandardPopupComponent,
        InternationalStandardDeletePopupComponent,
    ],
    entryComponents: [
        InternationalStandardComponent,
        InternationalStandardDialogComponent,
        InternationalStandardPopupComponent,
        InternationalStandardDeleteDialogComponent,
        InternationalStandardDeletePopupComponent,
    ],
    providers: [
        InternationalStandardService,
        InternationalStandardPopupService,
        InternationalStandardResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DescaInternationalStandardModule {}
