import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { TipoPregunta } from './tipo-pregunta.model';
import { TipoPreguntaService } from './tipo-pregunta.service';

@Component({
    selector: 'jhi-tipo-pregunta-detail',
    templateUrl: './tipo-pregunta-detail.component.html'
})
export class TipoPreguntaDetailComponent implements OnInit, OnDestroy {

    tipoPregunta: TipoPregunta;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private tipoPreguntaService: TipoPreguntaService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTipoPreguntas();
    }

    load(id) {
        this.tipoPreguntaService.find(id).subscribe((tipoPregunta) => {
            this.tipoPregunta = tipoPregunta;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTipoPreguntas() {
        this.eventSubscriber = this.eventManager.subscribe(
            'tipoPreguntaListModification',
            (response) => this.load(this.tipoPregunta.id)
        );
    }
}
