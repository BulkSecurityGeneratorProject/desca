import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { DescaMemberStateModule } from './member-state/member-state.module';
import { DescaDescaModule } from './desca/desca.module';
import { DescaApplicantModule } from './applicant/applicant.module';
import { DescaVulnerableGroupModule } from './vulnerable-group/vulnerable-group.module';
import { DescaMetodologyModule } from './metodology/metodology.module';
import { DescaInternationalStandarModule } from './international-standar/international-standar.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        DescaMemberStateModule,
        DescaDescaModule,
        DescaApplicantModule,
        DescaVulnerableGroupModule,
        DescaMetodologyModule,
        DescaInternationalStandarModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DescaEntityModule {}
