import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TipoPregunta } from './tipo-pregunta.model';
import { TipoPreguntaPopupService } from './tipo-pregunta-popup.service';
import { TipoPreguntaService } from './tipo-pregunta.service';

@Component({
    selector: 'jhi-tipo-pregunta-delete-dialog',
    templateUrl: './tipo-pregunta-delete-dialog.component.html'
})
export class TipoPreguntaDeleteDialogComponent {

    tipoPregunta: TipoPregunta;

    constructor(
        private tipoPreguntaService: TipoPreguntaService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.tipoPreguntaService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'tipoPreguntaListModification',
                content: 'Deleted an tipoPregunta'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-tipo-pregunta-delete-popup',
    template: ''
})
export class TipoPreguntaDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tipoPreguntaPopupService: TipoPreguntaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.tipoPreguntaPopupService
                .open(TipoPreguntaDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
