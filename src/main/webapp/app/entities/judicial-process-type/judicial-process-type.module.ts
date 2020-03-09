import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DescaSharedModule } from '../../shared';
import {
    JudicialProcessTypeService,
    JudicialProcessTypePopupService,
    JudicialProcessTypeComponent,
    JudicialProcessTypeDetailComponent,
    JudicialProcessTypeDialogComponent,
    JudicialProcessTypePopupComponent,
    JudicialProcessTypeDeletePopupComponent,
    JudicialProcessTypeDeleteDialogComponent,
    judicialProcessTypeRoute,
    judicialProcessTypePopupRoute,
    JudicialProcessTypeResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...judicialProcessTypeRoute,
    ...judicialProcessTypePopupRoute,
];

@NgModule({
    imports: [
        DescaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        JudicialProcessTypeComponent,
        JudicialProcessTypeDetailComponent,
        JudicialProcessTypeDialogComponent,
        JudicialProcessTypeDeleteDialogComponent,
        JudicialProcessTypePopupComponent,
        JudicialProcessTypeDeletePopupComponent,
    ],
    entryComponents: [
        JudicialProcessTypeComponent,
        JudicialProcessTypeDialogComponent,
        JudicialProcessTypePopupComponent,
        JudicialProcessTypeDeleteDialogComponent,
        JudicialProcessTypeDeletePopupComponent,
    ],
    providers: [
        JudicialProcessTypeService,
        JudicialProcessTypePopupService,
        JudicialProcessTypeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DescaJudicialProcessTypeModule {}
