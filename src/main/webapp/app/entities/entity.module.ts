import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { DescaMemberStateModule } from './member-state/member-state.module';
import { DescaDescaModule } from './desca/desca.module';
import { DescaApplicantModule } from './applicant/applicant.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        DescaMemberStateModule,
        DescaDescaModule,
        DescaApplicantModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DescaEntityModule {}
