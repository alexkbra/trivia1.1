import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Participar } from './participar.model';
import { ParticiparPopupService } from './participar-popup.service';
import { ParticiparService } from './participar.service';

@Component({
    selector: 'jhi-participar-delete-dialog',
    templateUrl: './participar-delete-dialog.component.html'
})
export class ParticiparDeleteDialogComponent {

    participar: Participar;

    constructor(
        private participarService: ParticiparService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.participarService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'participarListModification',
                content: 'Deleted an participar'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-participar-delete-popup',
    template: ''
})
export class ParticiparDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private participarPopupService: ParticiparPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.participarPopupService
                .open(ParticiparDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
