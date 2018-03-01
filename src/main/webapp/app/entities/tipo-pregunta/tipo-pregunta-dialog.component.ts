import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TipoPregunta } from './tipo-pregunta.model';
import { TipoPreguntaPopupService } from './tipo-pregunta-popup.service';
import { TipoPreguntaService } from './tipo-pregunta.service';

@Component({
    selector: 'jhi-tipo-pregunta-dialog',
    templateUrl: './tipo-pregunta-dialog.component.html'
})
export class TipoPreguntaDialogComponent implements OnInit {

    tipoPregunta: TipoPregunta;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private tipoPreguntaService: TipoPreguntaService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.tipoPregunta.id !== undefined) {
            this.subscribeToSaveResponse(
                this.tipoPreguntaService.update(this.tipoPregunta));
        } else {
            this.subscribeToSaveResponse(
                this.tipoPreguntaService.create(this.tipoPregunta));
        }
    }

    private subscribeToSaveResponse(result: Observable<TipoPregunta>) {
        result.subscribe((res: TipoPregunta) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: TipoPregunta) {
        this.eventManager.broadcast({ name: 'tipoPreguntaListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-tipo-pregunta-popup',
    template: ''
})
export class TipoPreguntaPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tipoPreguntaPopupService: TipoPreguntaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.tipoPreguntaPopupService
                    .open(TipoPreguntaDialogComponent as Component, params['id']);
            } else {
                this.tipoPreguntaPopupService
                    .open(TipoPreguntaDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
