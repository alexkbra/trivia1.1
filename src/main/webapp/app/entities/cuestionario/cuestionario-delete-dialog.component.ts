import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Cuestionario } from './cuestionario.model';
import { CuestionarioPopupService } from './cuestionario-popup.service';
import { CuestionarioService } from './cuestionario.service';

@Component({
    selector: 'jhi-cuestionario-delete-dialog',
    templateUrl: './cuestionario-delete-dialog.component.html'
})
export class CuestionarioDeleteDialogComponent {

    cuestionario: Cuestionario;

    constructor(
        private cuestionarioService: CuestionarioService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cuestionarioService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'cuestionarioListModification',
                content: 'Deleted an cuestionario'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cuestionario-delete-popup',
    template: ''
})
export class CuestionarioDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cuestionarioPopupService: CuestionarioPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.cuestionarioPopupService
                .open(CuestionarioDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
