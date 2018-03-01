import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Empresas } from './empresas.model';
import { EmpresasService } from './empresas.service';

@Component({
    selector: 'jhi-empresas-detail',
    templateUrl: './empresas-detail.component.html'
})
export class EmpresasDetailComponent implements OnInit, OnDestroy {

    empresas: Empresas;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private empresasService: EmpresasService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInEmpresas();
    }

    load(id) {
        this.empresasService.find(id).subscribe((empresas) => {
            this.empresas = empresas;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInEmpresas() {
        this.eventSubscriber = this.eventManager.subscribe(
            'empresasListModification',
            (response) => this.load(this.empresas.id)
        );
    }
}
