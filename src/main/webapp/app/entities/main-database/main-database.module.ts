import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DescaSharedModule } from '../../shared';
import {
    MainDatabaseService,
    MainDatabasePopupService,
    MainDatabaseComponent,
    MainDatabaseDetailComponent,
    MainDatabaseDialogComponent,
    MainDatabasePopupComponent,
    MainDatabaseDeletePopupComponent,
    MainDatabaseDeleteDialogComponent,
    mainDatabaseRoute,
    mainDatabasePopupRoute,
    MainDatabaseResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...mainDatabaseRoute,
    ...mainDatabasePopupRoute,
];

@NgModule({
    imports: [
        DescaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        MainDatabaseComponent,
        MainDatabaseDetailComponent,
        MainDatabaseDialogComponent,
        MainDatabaseDeleteDialogComponent,
        MainDatabasePopupComponent,
        MainDatabaseDeletePopupComponent,
    ],
    entryComponents: [
        MainDatabaseComponent,
        MainDatabaseDialogComponent,
        MainDatabasePopupComponent,
        MainDatabaseDeleteDialogComponent,
        MainDatabaseDeletePopupComponent,
    ],
    providers: [
        MainDatabaseService,
        MainDatabasePopupService,
        MainDatabaseResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DescaMainDatabaseModule {}
