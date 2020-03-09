import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DescaSharedModule } from '../../shared';
import {
    InternationalStandarService,
    InternationalStandarPopupService,
    InternationalStandarComponent,
    InternationalStandarDetailComponent,
    InternationalStandarDialogComponent,
    InternationalStandarPopupComponent,
    InternationalStandarDeletePopupComponent,
    InternationalStandarDeleteDialogComponent,
    internationalStandarRoute,
    internationalStandarPopupRoute,
    InternationalStandarResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...internationalStandarRoute,
    ...internationalStandarPopupRoute,
];

@NgModule({
    imports: [
        DescaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        InternationalStandarComponent,
        InternationalStandarDetailComponent,
        InternationalStandarDialogComponent,
        InternationalStandarDeleteDialogComponent,
        InternationalStandarPopupComponent,
        InternationalStandarDeletePopupComponent,
    ],
    entryComponents: [
        InternationalStandarComponent,
        InternationalStandarDialogComponent,
        InternationalStandarPopupComponent,
        InternationalStandarDeleteDialogComponent,
        InternationalStandarDeletePopupComponent,
    ],
    providers: [
        InternationalStandarService,
        InternationalStandarPopupService,
        InternationalStandarResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DescaInternationalStandarModule {}
