import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Pregunta } from './pregunta.model';
import { PreguntaPopupService } from './pregunta-popup.service';
import { PreguntaService } from './pregunta.service';

@Component({
    selector: 'jhi-pregunta-delete-dialog',
    templateUrl: './pregunta-delete-dialog.component.html'
})
export class PreguntaDeleteDialogComponent {

    pregunta: Pregunta;

    constructor(
        private preguntaService: PreguntaService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.preguntaService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'preguntaListModification',
                content: 'Deleted an pregunta'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-pregunta-delete-popup',
    template: ''
})
export class PreguntaDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private preguntaPopupService: PreguntaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.preguntaPopupService
                .open(PreguntaDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
