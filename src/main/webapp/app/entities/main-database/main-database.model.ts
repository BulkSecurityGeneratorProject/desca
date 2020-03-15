import { BaseEntity } from './../../shared';

export class MainDatabase implements BaseEntity {
    constructor(
        public id?: number,
        public number?: string,
        public intitution?: string,
        public memberStateId?: number,
        public judicialProcessTypeId?: number,
    ) {
    }
}
