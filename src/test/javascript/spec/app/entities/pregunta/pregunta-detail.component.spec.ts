/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TriviaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PreguntaDetailComponent } from '../../../../../../main/webapp/app/entities/pregunta/pregunta-detail.component';
import { PreguntaService } from '../../../../../../main/webapp/app/entities/pregunta/pregunta.service';
import { Pregunta } from '../../../../../../main/webapp/app/entities/pregunta/pregunta.model';

describe('Component Tests', () => {

    describe('Pregunta Management Detail Component', () => {
        let comp: PreguntaDetailComponent;
        let fixture: ComponentFixture<PreguntaDetailComponent>;
        let service: PreguntaService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TriviaTestModule],
                declarations: [PreguntaDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PreguntaService,
                    JhiEventManager
                ]
            }).overrideTemplate(PreguntaDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PreguntaDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PreguntaService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Pregunta(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.pregunta).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
