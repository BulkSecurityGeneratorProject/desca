import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DescaSharedModule } from '../../shared';
import {
    MetodologyService,
    MetodologyPopupService,
    MetodologyComponent,
    MetodologyDetailComponent,
    MetodologyDialogComponent,
    MetodologyPopupComponent,
    MetodologyDeletePopupComponent,
    MetodologyDeleteDialogComponent,
    metodologyRoute,
    metodologyPopupRoute,
    MetodologyResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...metodologyRoute,
    ...metodologyPopupRoute,
];

@NgModule({
    imports: [
        DescaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        MetodologyComponent,
        MetodologyDetailComponent,
        MetodologyDialogComponent,
        MetodologyDeleteDialogComponent,
        MetodologyPopupComponent,
        MetodologyDeletePopupComponent,
    ],
    entryComponents: [
        MetodologyComponent,
        MetodologyDialogComponent,
        MetodologyPopupComponent,
        MetodologyDeleteDialogComponent,
        MetodologyDeletePopupComponent,
    ],
    providers: [
        MetodologyService,
        MetodologyPopupService,
        MetodologyResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DescaMetodologyModule {}
