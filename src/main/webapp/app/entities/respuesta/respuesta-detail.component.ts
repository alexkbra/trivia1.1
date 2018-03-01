import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Respuesta } from './respuesta.model';
import { RespuestaService } from './respuesta.service';

@Component({
    selector: 'jhi-respuesta-detail',
    templateUrl: './respuesta-detail.component.html'
})
export class RespuestaDetailComponent implements OnInit, OnDestroy {

    respuesta: Respuesta;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private respuestaService: RespuestaService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRespuestas();
    }

    load(id) {
        this.respuestaService.find(id).subscribe((respuesta) => {
            this.respuesta = respuesta;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRespuestas() {
        this.eventSubscriber = this.eventManager.subscribe(
            'respuestaListModification',
            (response) => this.load(this.respuesta.id)
        );
    }
}
