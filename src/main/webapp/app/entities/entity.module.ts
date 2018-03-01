import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { TriviaEmpresasModule } from './empresas/empresas.module';
import { TriviaGaleriasModule } from './galerias/galerias.module';
import { TriviaPublicidadModule } from './publicidad/publicidad.module';
import { TriviaExpedicionModule } from './expedicion/expedicion.module';
import { TriviaCuestionarioModule } from './cuestionario/cuestionario.module';
import { TriviaReglaModule } from './regla/regla.module';
import { TriviaNivelModule } from './nivel/nivel.module';
import { TriviaTipoPreguntaModule } from './tipo-pregunta/tipo-pregunta.module';
import { TriviaPreguntaModule } from './pregunta/pregunta.module';
import { TriviaPistaModule } from './pista/pista.module';
import { TriviaRespuestaModule } from './respuesta/respuesta.module';
import { TriviaParticiparModule } from './participar/participar.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        TriviaEmpresasModule,
        TriviaGaleriasModule,
        TriviaPublicidadModule,
        TriviaExpedicionModule,
        TriviaCuestionarioModule,
        TriviaReglaModule,
        TriviaNivelModule,
        TriviaTipoPreguntaModule,
        TriviaPreguntaModule,
        TriviaPistaModule,
        TriviaRespuestaModule,
        TriviaParticiparModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TriviaEntityModule {}
