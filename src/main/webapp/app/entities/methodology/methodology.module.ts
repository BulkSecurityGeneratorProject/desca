import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DescaSharedModule } from '../../shared';
import {
    MethodologyService,
    MethodologyPopupService,
    MethodologyComponent,
    MethodologyDetailComponent,
    MethodologyDialogComponent,
    MethodologyPopupComponent,
    MethodologyDeletePopupComponent,
    MethodologyDeleteDialogComponent,
    methodologyRoute,
    methodologyPopupRoute,
    MethodologyResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...methodologyRoute,
    ...methodologyPopupRoute,
];

@NgModule({
    imports: [
        DescaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        MethodologyComponent,
        MethodologyDetailComponent,
        MethodologyDialogComponent,
        MethodologyDeleteDialogComponent,
        MethodologyPopupComponent,
        MethodologyDeletePopupComponent,
    ],
    entryComponents: [
        MethodologyComponent,
        MethodologyDialogComponent,
        MethodologyPopupComponent,
        MethodologyDeleteDialogComponent,
        MethodologyDeletePopupComponent,
    ],
    providers: [
        MethodologyService,
        MethodologyPopupService,
        MethodologyResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DescaMethodologyModule {}
