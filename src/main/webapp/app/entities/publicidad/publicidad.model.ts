import { BaseEntity } from './../../shared';

export class Publicidad implements BaseEntity {
    constructor(
        public id?: number,
        public titulo?: string,
        public descripcion?: string,
        public premiodescripcion?: string,
        public galerias?: BaseEntity[],
        public empresasId?: number,
        public expedicions?: BaseEntity[],
    ) {
    }
}
