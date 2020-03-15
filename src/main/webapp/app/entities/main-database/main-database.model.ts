import { BaseEntity } from './../../shared';

export class MainDatabase implements BaseEntity {
    constructor(
        public id?: number,
        public number?: string,
        public intitution?: string,
        public resolutionDate?: any,
        public memberStateId?: number,
        public judicialProcessTypeId?: number,
        public descaWayByCId?: number,
    ) {
    }
}
