import { BaseEntity } from './../../shared';

export class JudicialProcessType implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public status?: boolean,
    ) {
        this.status = false;
    }
}
