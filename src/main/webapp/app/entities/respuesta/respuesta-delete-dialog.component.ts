import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Respuesta } from './respuesta.model';
import { RespuestaPopupService } from './respuesta-popup.service';
import { RespuestaService } from './respuesta.service';

@Component({
    selector: 'jhi-respuesta-delete-dialog',
    templateUrl: './respuesta-delete-dialog.component.html'
})
export class RespuestaDeleteDialogComponent {

    respuesta: Respuesta;

    constructor(
        private respuestaService: RespuestaService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.respuestaService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'respuestaListModification',
                content: 'Deleted an respuesta'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-respuesta-delete-popup',
    template: ''
})
export class RespuestaDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private respuestaPopupService: RespuestaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.respuestaPopupService
                .open(RespuestaDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
