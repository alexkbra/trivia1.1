import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Pregunta } from './pregunta.model';
import { PreguntaPopupService } from './pregunta-popup.service';
import { PreguntaService } from './pregunta.service';
import { Cuestionario, CuestionarioService } from '../cuestionario';
import { TipoPregunta, TipoPreguntaService } from '../tipo-pregunta';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-pregunta-dialog',
    templateUrl: './pregunta-dialog.component.html'
})
export class PreguntaDialogComponent implements OnInit {

    pregunta: Pregunta;
    isSaving: boolean;

    cuestionarios: Cuestionario[];

    tipopreguntas: TipoPregunta[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private preguntaService: PreguntaService,
        private cuestionarioService: CuestionarioService,
        private tipoPreguntaService: TipoPreguntaService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.cuestionarioService.query()
            .subscribe((res: ResponseWrapper) => { this.cuestionarios = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.tipoPreguntaService.query()
            .subscribe((res: ResponseWrapper) => { this.tipopreguntas = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.pregunta.id !== undefined) {
            this.subscribeToSaveResponse(
                this.preguntaService.update(this.pregunta));
        } else {
            this.subscribeToSaveResponse(
                this.preguntaService.create(this.pregunta));
        }
    }

    private subscribeToSaveResponse(result: Observable<Pregunta>) {
        result.subscribe((res: Pregunta) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Pregunta) {
        this.eventManager.broadcast({ name: 'preguntaListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackCuestionarioById(index: number, item: Cuestionario) {
        return item.id;
    }

    trackTipoPreguntaById(index: number, item: TipoPregunta) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-pregunta-popup',
    template: ''
})
export class PreguntaPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private preguntaPopupService: PreguntaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.preguntaPopupService
                    .open(PreguntaDialogComponent as Component, params['id']);
            } else {
                this.preguntaPopupService
                    .open(PreguntaDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
