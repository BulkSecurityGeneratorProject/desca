import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DescaSharedModule } from '../../shared';
import {
    DescaService,
    DescaPopupService,
    DescaComponent,
    DescaDetailComponent,
    DescaDialogComponent,
    DescaPopupComponent,
    DescaDeletePopupComponent,
    DescaDeleteDialogComponent,
    descaRoute,
    descaPopupRoute,
    DescaResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...descaRoute,
    ...descaPopupRoute,
];

@NgModule({
    imports: [
        DescaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        DescaComponent,
        DescaDetailComponent,
        DescaDialogComponent,
        DescaDeleteDialogComponent,
        DescaPopupComponent,
        DescaDeletePopupComponent,
    ],
    entryComponents: [
        DescaComponent,
        DescaDialogComponent,
        DescaPopupComponent,
        DescaDeleteDialogComponent,
        DescaDeletePopupComponent,
    ],
    providers: [
        DescaService,
        DescaPopupService,
        DescaResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DescaDescaModule {}
