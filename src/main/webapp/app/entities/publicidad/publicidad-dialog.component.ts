import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Publicidad } from './publicidad.model';
import { PublicidadPopupService } from './publicidad-popup.service';
import { PublicidadService } from './publicidad.service';
import { Empresas, EmpresasService } from '../empresas';
import { Expedicion, ExpedicionService } from '../expedicion';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-publicidad-dialog',
    templateUrl: './publicidad-dialog.component.html'
})
export class PublicidadDialogComponent implements OnInit {

    publicidad: Publicidad;
    isSaving: boolean;

    empresas: Empresas[];

    expedicions: Expedicion[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private publicidadService: PublicidadService,
        private empresasService: EmpresasService,
        private expedicionService: ExpedicionService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.empresasService.query()
            .subscribe((res: ResponseWrapper) => { this.empresas = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.expedicionService.query()
            .subscribe((res: ResponseWrapper) => { this.expedicions = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.publicidad.id !== undefined) {
            this.subscribeToSaveResponse(
                this.publicidadService.update(this.publicidad));
        } else {
            this.subscribeToSaveResponse(
                this.publicidadService.create(this.publicidad));
        }
    }

    private subscribeToSaveResponse(result: Observable<Publicidad>) {
        result.subscribe((res: Publicidad) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Publicidad) {
        this.eventManager.broadcast({ name: 'publicidadListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackEmpresasById(index: number, item: Empresas) {
        return item.id;
    }

    trackExpedicionById(index: number, item: Expedicion) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-publicidad-popup',
    template: ''
})
export class PublicidadPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private publicidadPopupService: PublicidadPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.publicidadPopupService
                    .open(PublicidadDialogComponent as Component, params['id']);
            } else {
                this.publicidadPopupService
                    .open(PublicidadDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
