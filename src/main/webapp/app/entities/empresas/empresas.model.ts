import { BaseEntity } from './../../shared';

export class Empresas implements BaseEntity {
    constructor(
        public id?: number,
        public nombre?: string,
        public razonSocial?: string,
        public publicidads?: BaseEntity[],
    ) {
    }
}
