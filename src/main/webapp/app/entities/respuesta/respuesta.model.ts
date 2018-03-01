import { BaseEntity } from './../../shared';

export class Respuesta implements BaseEntity {
    constructor(
        public id?: number,
        public cortaDescripcion?: string,
        public descripcion?: string,
        public preguntaId?: number,
    ) {
    }
}
