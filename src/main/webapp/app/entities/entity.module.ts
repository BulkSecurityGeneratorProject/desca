import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { DescaApplicantModule } from './applicant/applicant.module';
import { DescaDescaModule } from './desca/desca.module';
import { DescaDescaWayModule } from './desca-way/desca-way.module';
import { DescaInternationalStandardModule } from './international-standard/international-standard.module';
import { DescaJudicialProcessTypeModule } from './judicial-process-type/judicial-process-type.module';
import { DescaMemberStateModule } from './member-state/member-state.module';
import { DescaMethodologyModule } from './methodology/methodology.module';
import { DescaVulnerableGroupModule } from './vulnerable-group/vulnerable-group.module';
import { DescaRepairTypeModule } from './repair-type/repair-type.module';
import { DescaRepairModule } from './repair/repair.module';
import { DescaMainDatabaseModule } from './main-database/main-database.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        DescaApplicantModule,
        DescaDescaModule,
        DescaDescaWayModule,
        DescaInternationalStandardModule,
        DescaJudicialProcessTypeModule,
        DescaMemberStateModule,
        DescaMethodologyModule,
        DescaVulnerableGroupModule,
        DescaRepairTypeModule,
        DescaRepairModule,
        DescaMainDatabaseModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DescaEntityModule {}
