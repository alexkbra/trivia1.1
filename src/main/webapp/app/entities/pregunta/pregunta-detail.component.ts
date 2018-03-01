import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Pregunta } from './pregunta.model';
import { PreguntaService } from './pregunta.service';

@Component({
    selector: 'jhi-pregunta-detail',
    templateUrl: './pregunta-detail.component.html'
})
export class PreguntaDetailComponent implements OnInit, OnDestroy {

    pregunta: Pregunta;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private preguntaService: PreguntaService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPreguntas();
    }

    load(id) {
        this.preguntaService.find(id).subscribe((pregunta) => {
            this.pregunta = pregunta;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPreguntas() {
        this.eventSubscriber = this.eventManager.subscribe(
            'preguntaListModification',
            (response) => this.load(this.pregunta.id)
        );
    }
}
